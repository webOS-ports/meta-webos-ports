#!/usr/bin/env python3
"""Run the existing Android pre-start hook with the new device-specific rule."""

import importlib.util
import os
from pathlib import Path
import shutil
import stat
import subprocess
import sys
import tempfile
import unittest
from types import SimpleNamespace

REPO = Path(__file__).resolve().parent.parent
FIXTURES = REPO / "tests/fixtures"
RULE = REPO / "meta-luneos/recipes-core/android-system/android-system/stubbed-services-tangorpro"
REAL_MOUNTS = "--real-mounts" in sys.argv
if REAL_MOUNTS:
    sys.argv.remove("--real-mounts")

spec = importlib.util.spec_from_file_location("stager", REPO / "tools/stage-tangorpro-workaround.py")
stager = importlib.util.module_from_spec(spec)
spec.loader.exec_module(stager)
spec = importlib.util.spec_from_file_location("diagnostic", REPO / "tools/check-tangorpro-persist.py")
diagnostic = importlib.util.module_from_spec(spec)
spec.loader.exec_module(diagnostic)


class PersistDiagnosticTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.root = Path(self.tmp.name)
        self.mounts = self.root / "mounts"
        self.config = self.root / "config"
        self.device_dir = self.root / "by-name"
        self.config.write_text("lxc.mount.entry = /mnt mnt bind rbind 0 0\n")
        self.mounts.write_text("/dev/sda1 /mnt/vendor/persist ext4 rw,nosuid,nodev 0 0\n")

    def check(self, wrong_source=False):
        def fake_stat(path):
            number = os.makedev(8, 2 if wrong_source and path == "/dev/sda1" else 1)
            return SimpleNamespace(st_mode=stat.S_IFBLK | 0o600, st_rdev=number)
        return diagnostic.check_persist(mounts=self.mounts, config=self.config,
                                        device_dirs=[self.device_dir], stat_fn=fake_stat)

    def test_partition_identity_accepts_different_names_for_same_device(self):
        self.assertIn("verified writable ext4", self.check())

    def test_missing_mount_is_not_confused_with_a_directory(self):
        self.mounts.write_text("/dev/sda1 / ext4 rw 0 0\n")
        with self.assertRaisesRegex(diagnostic.PersistError, "not mounted"):
            self.check()

    def test_wrong_partition_is_rejected(self):
        with self.assertRaisesRegex(diagnostic.PersistError, "not backed"):
            self.check(wrong_source=True)

    def test_read_only_mount_is_reported(self):
        self.mounts.write_text("/dev/sda1 /mnt/vendor/persist ext4 ro,nosuid 0 0\n")
        with self.assertRaisesRegex(diagnostic.PersistError, "read-only"):
            self.check()

    def test_later_plain_bind_cannot_hide_loss_of_submounts(self):
        with self.config.open("a") as out:
            out.write("lxc.mount.entry = /mnt mnt bind bind 0 0\n")
        with self.assertRaisesRegex(diagnostic.PersistError, "recursively"):
            self.check()

    def test_vendor_identity_is_read_without_a_running_container(self):
        vendor = self.root / "vendor"
        vendor.mkdir()
        (vendor / "build.prop").write_text("ro.product.device=halium_arm64\nro.product.vendor.device=tangorpro\n")
        self.assertEqual(diagnostic.codename(self.root), "tangorpro")


class WorkaroundTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.base = Path(self.tmp.name)
        self.root = self.base / "luneos"
        self.runtime = self.root / "var/lib/lxc/android"
        self.container = self.base / "android"
        for directory in [self.runtime / "stubbed-services.d", self.runtime / "stubs",
                          self.runtime / "pre-start.d", self.container / "vendor/bin",
                          self.container / "system"]:
            directory.mkdir(parents=True)
        for name, relative in [("50-stub-services", "pre-start.d/50-stub-services"),
                               ("stub-sleep", "stubs/stub-sleep")]:
            dest = self.runtime / relative
            shutil.copyfile(FIXTURES / name, dest)
            dest.chmod(0o755)
        (self.runtime / "pre-start.sh").write_text(
            "#!/bin/sh\nrun-parts /var/lib/lxc/android/pre-start.d || true\n")
        marker = self.root / "usr/share/luneos/adaptations/tangorpro/deviceinfo"
        marker.parent.mkdir(parents=True)
        marker.write_text('deviceinfo_touch_rotate="0"\n')
        self.original = b"ORIGINAL VENDOR STORAGE PROXY\n"
        self.proxy = self.container / "vendor/bin/storageproxyd"
        self.proxy.write_bytes(self.original)
        self.mountlog = self.base / "mounts"
        self.fakebin = self.base / "bin"
        self.fakebin.mkdir()
        mount = self.fakebin / "mount"
        mount.write_text('#!/bin/sh\nprintf "%s\\n" "$@" >> "$MOUNT_LOG"\n')
        mount.chmod(0o755)

    def props(self, codename, key="ro.product.vendor.device"):
        (self.container / "vendor/build.prop").write_text(f"{key}={codename}\n")
        (self.container / "system/build.prop").write_text("ro.product.device=halium_arm64\n")

    def hook(self, real=False):
        # Redirect only the upstream script's host runtime directory. Its
        # codename selection, list parsing, and mount call remain unchanged.
        hook = self.base / "50-stub-services"
        hook.write_text((FIXTURES / "50-stub-services").read_text().replace(
            "/var/lib/lxc/android", str(self.runtime)))
        env = dict(os.environ, LXC_ROOTFS_PATH=str(self.container), MOUNT_LOG=str(self.mountlog))
        if not real:
            env["PATH"] = str(self.fakebin) + os.pathsep + env["PATH"]
        return subprocess.run(["sh", str(hook)], env=env, capture_output=True, text=True, check=True)

    def test_tangorpro_is_intercepted_before_vendor_daemon_runs(self):
        self.props("tangorpro")
        stager.stage(self.root, "apply")
        self.hook()
        args = self.mountlog.read_text().splitlines()
        self.assertEqual(args[0], "--bind")
        self.assertEqual(Path(args[1]), self.runtime / "stubs/stub-sleep")
        self.assertEqual(Path(args[2]), self.proxy)
        self.assertEqual(len(args), 3)
        self.assertEqual(self.proxy.read_bytes(), self.original)

    def test_alternate_vendor_property_selects_tablet(self):
        self.props("tangorpro", "ro.vendor.product.device")
        stager.stage(self.root, "apply")
        self.hook()
        self.assertTrue(self.mountlog.exists())

    def test_other_devices_keep_the_real_storage_proxy(self):
        stager.stage(self.root, "apply")
        for device in ["sargo", "bluejay", "panther", "tangorpro_other"]:
            with self.subTest(device=device):
                self.props(device)
                self.hook()
                self.assertFalse(self.mountlog.exists())
                self.assertEqual(self.proxy.read_bytes(), self.original)

    def test_staging_is_idempotent_and_removable(self):
        stager.stage(self.root, "apply")
        target = self.root / stager.DEST
        first = target.stat().st_mtime_ns
        stager.stage(self.root, "apply")
        self.assertEqual(target.stat().st_mtime_ns, first)
        self.assertEqual(target.stat().st_mode & 0o777, 0o644)
        stager.stage(self.root, "remove")
        self.assertFalse(target.exists())

    def test_unsupported_startup_hook_is_rejected_without_changes(self):
        (self.runtime / "pre-start.d/50-stub-services").write_text("#!/bin/sh\nexit 0\n")
        with self.assertRaisesRegex(ValueError, "Unsupported"):
            stager.stage(self.root, "apply")
        self.assertFalse((self.root / stager.DEST).exists())

    def test_existing_custom_rules_are_not_overwritten_or_removed(self):
        target = self.root / stager.DEST
        target.write_text("/vendor/bin/a-different-service\n")
        for action in ["apply", "remove"]:
            with self.subTest(action=action), self.assertRaisesRegex(ValueError, "existing"):
                stager.stage(self.root, action)
        self.assertEqual(target.read_text(), "/vendor/bin/a-different-service\n")

    def test_symlink_outside_rootfs_is_rejected(self):
        directory = self.runtime / "stubbed-services.d"
        directory.rmdir()
        outside = self.base / "outside"
        outside.mkdir()
        directory.symlink_to(outside, target_is_directory=True)
        with self.assertRaisesRegex(ValueError, "escapes"):
            stager.stage(self.root, "apply")
        self.assertEqual(list(outside.iterdir()), [])

    def test_running_root_is_rejected(self):
        with self.assertRaisesRegex(ValueError, "offline"):
            stager.stage(Path("/"), "apply")

    @unittest.skipUnless(REAL_MOUNTS, "needs Linux root in a private mount namespace")
    def test_real_bind_mount_over_read_only_vendor_is_reversible(self):
        self.props("tangorpro")
        stager.stage(self.root, "apply")
        vendor = self.container / "vendor"
        subprocess.run(["mount", "--bind", str(vendor), str(vendor)], check=True)
        file_mounted = False
        try:
            subprocess.run(["mount", "-o", "remount,bind,ro", str(vendor)], check=True)
            self.hook(real=True)
            file_mounted = True
            self.assertEqual(self.proxy.read_bytes(), (FIXTURES / "stub-sleep").read_bytes())
            subprocess.run(["umount", str(self.proxy)], check=True)
            file_mounted = False
            self.assertEqual(self.proxy.read_bytes(), self.original)
        finally:
            if file_mounted:
                subprocess.run(["umount", str(self.proxy)], check=True)
            subprocess.run(["umount", str(vendor)], check=True)


if __name__ == "__main__":
    unittest.main(verbosity=2)
