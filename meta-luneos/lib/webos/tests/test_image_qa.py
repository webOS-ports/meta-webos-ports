# Unit tests for webos/image_qa.py. Run without bitbake:
#   python3 meta-luneos/lib/webos/tests/test_image_qa.py -v
# The crafted trees go into TMPDIR (tempfile's default).
#
# SPDX-License-Identifier: MIT

import os
import shutil
import stat
import sys
import tempfile
import unittest

sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", ".."))
from webos import image_qa  # noqa: E402


class ImageQaTest(unittest.TestCase):
    def setUp(self):
        self.root = tempfile.mkdtemp(prefix="image-qa-")
        for d in ("usr/bin", "usr/sbin", "usr/libexec", "etc", "var/db",
                  "media", "tmp"):
            os.makedirs(os.path.join(self.root, d))
        # usrmerge layout: /bin and /sbin are symlinks into /usr
        os.symlink("usr/bin", os.path.join(self.root, "bin"))
        os.symlink("usr/sbin", os.path.join(self.root, "sbin"))
        for d in ("", "usr", "usr/bin", "usr/sbin", "usr/libexec", "etc",
                  "var", "var/db", "media"):
            os.chmod(os.path.join(self.root, d), 0o755)
        os.chmod(os.path.join(self.root, "tmp"), 0o1777)
        self.file("usr/bin/tool", 0o755)
        self.file("etc/config", 0o644)
        self.file("usr/bin/passwd", 0o4755)

    def tearDown(self):
        shutil.rmtree(self.root)

    def file(self, rel, mode):
        p = os.path.join(self.root, rel)
        with open(p, "w") as f:
            f.write("x")
        os.chmod(p, mode)
        return p

    def scan(self, **kw):
        kw.setdefault("suid_allow", ["/usr/bin/passwd"])
        return image_qa.scan(self.root, **kw)

    def paths(self, entries):
        return [e[0] for e in entries]

    def test_clean_tree_passes(self):
        self.assertEqual(self.scan(), ([], []))

    def test_world_writable_file_in_system_dirs_fails(self):
        self.file("usr/sbin/webos-system-update", 0o777)
        self.file("etc/writable.conf", 0o666)
        errors, _ = self.scan()
        self.assertEqual(self.paths(errors),
                         ["/etc/writable.conf", "/usr/sbin/webos-system-update"])

    def test_world_writable_file_elsewhere_warns(self):
        self.file("var/db/ntp-kod", 0o666)
        errors, warnings = self.scan()
        self.assertEqual(errors, [])
        self.assertEqual(self.paths(warnings), ["/var/db/ntp-kod"])

    def test_world_writable_file_allow_list(self):
        self.file("var/db/ntp-kod", 0o666)
        self.assertEqual(self.scan(ww_file_allow=["/var/db/ntp-kod"]), ([], []))

    def test_world_writable_dir_without_sticky_fails(self):
        d = os.path.join(self.root, "media/internal")
        os.mkdir(d)
        os.chmod(d, 0o777)
        errors, _ = self.scan()
        self.assertEqual(self.paths(errors), ["/media/internal"])

    def test_sticky_world_writable_dir_passes(self):
        # /tmp in setUp is 1777
        d = os.path.join(self.root, "media/shared")
        os.mkdir(d)
        os.chmod(d, 0o1777)
        self.assertEqual(self.scan(), ([], []))

    def test_world_writable_dir_allow_list(self):
        d = os.path.join(self.root, "media/internal")
        os.mkdir(d)
        os.chmod(d, 0o777)
        self.assertEqual(self.scan(ww_dir_allow=["media/*"]), ([], []))

    def test_group_writable_dir_passes(self):
        d = os.path.join(self.root, "media/internal")
        os.mkdir(d)
        os.chmod(d, 0o775)
        self.assertEqual(self.scan(), ([], []))

    def test_setuid_not_allowed_fails(self):
        self.file("usr/bin/screen-5.0.1", 0o4755)
        errors, _ = self.scan()
        self.assertEqual(errors, [("/usr/bin/screen-5.0.1", "4755",
                                   "setuid, not in the allow list")])

    def test_setgid_not_allowed_fails(self):
        self.file("usr/libexec/helper", 0o2755)
        errors, _ = self.scan()
        self.assertEqual(self.paths(errors), ["/usr/libexec/helper"])

    def test_setgid_without_group_exec_is_not_a_privilege(self):
        self.file("usr/libexec/locked", 0o2644)
        self.assertEqual(self.scan(), ([], []))

    def test_setuid_allow_list_pattern(self):
        self.file("usr/libexec/dbus-daemon-launch-helper", 0o4754)
        errors, _ = self.scan(suid_allow=["/usr/bin/passwd", "/usr/libexec/dbus-*"])
        self.assertEqual(errors, [])

    def test_setuid_allow_list_is_exact_by_default(self):
        self.file("usr/bin/passwd2", 0o4755)
        errors, _ = self.scan()
        self.assertEqual(self.paths(errors), ["/usr/bin/passwd2"])

    def test_symlinks_are_not_followed(self):
        # a symlink to a world-writable target outside the rootfs, and the
        # usrmerge links, must not be reported or walked twice
        outside = tempfile.mkdtemp(prefix="image-qa-outside-")
        try:
            os.chmod(outside, 0o777)
            os.symlink(outside, os.path.join(self.root, "usr/bin/link"))
            self.file("usr/bin/bad", 0o777)
            errors, _ = self.scan()
            self.assertEqual(self.paths(errors), ["/usr/bin/bad"])
        finally:
            os.chmod(outside, 0o755)
            os.rmdir(outside)

    def test_without_usrmerge_bin_is_a_system_dir(self):
        os.unlink(os.path.join(self.root, "bin"))
        os.mkdir(os.path.join(self.root, "bin"))
        os.chmod(os.path.join(self.root, "bin"), 0o755)
        self.file("bin/sh", 0o757)
        errors, _ = self.scan()
        self.assertEqual(self.paths(errors), ["/bin/sh"])

    def test_root_itself_is_checked(self):
        os.chmod(self.root, 0o777)
        try:
            errors, _ = self.scan()
            self.assertEqual(self.paths(errors), ["/"])
        finally:
            os.chmod(self.root, 0o755)

    def test_format_report(self):
        self.file("usr/bin/screen", 0o4755)
        errors, _ = self.scan()
        self.assertEqual(image_qa.format_report(errors),
                         "  4755 /usr/bin/screen: setuid, not in the allow list")


if __name__ == "__main__":
    unittest.main()
