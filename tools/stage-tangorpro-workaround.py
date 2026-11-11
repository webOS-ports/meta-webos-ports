#!/usr/bin/env python3
"""Check or stage the tangorpro workaround in an OFFLINE LuneOS rootfs."""

import argparse
import hashlib
import os
from pathlib import Path
import sys
import tempfile

REPO = Path(__file__).resolve().parent.parent
RULE = REPO / "meta-luneos/recipes-core/android-system/android-system/stubbed-services-tangorpro"
DEST = "var/lib/lxc/android/stubbed-services.d/tangorpro"
FIXTURES = REPO / "tests/fixtures"


def inside(root, relative):
    path = (root / relative).resolve()
    if not path.is_relative_to(root):
        raise ValueError(f"Path escapes the offline rootfs through a symlink: {relative}")
    return path


def validate(root):
    root = root.resolve(strict=True)
    if not root.is_dir() or root == Path("/"):
        raise ValueError("Supply an offline LuneOS rootfs directory, never the running /")
    # The public shared Halium rootfs predates the tangorpro adaptation file,
    # but already has the per-codename stub hook. The rule does not depend on
    # luneos-device-config; identify the OS by its own release marker instead.
    markers = [inside(root, "etc/luneos-release"), inside(root, "etc/webos-release")]
    if not any(marker.is_file() for marker in markers):
        raise ValueError("This rootfs has no LuneOS release marker")
    for relative, fixture in [
        ("var/lib/lxc/android/pre-start.d/50-stub-services", "50-stub-services"),
        ("var/lib/lxc/android/stubs/stub-sleep", "stub-sleep"),
    ]:
        target = inside(root, relative)
        expected = (FIXTURES / fixture).read_bytes()
        if not target.is_file() or target.read_bytes() != expected:
            raise ValueError(f"Unsupported or missing {relative}; refusing to guess its behaviour")
        if not os.access(target, os.X_OK):
            raise ValueError(f"The startup file is not executable: {relative}")
    pre_start = inside(root, "var/lib/lxc/android/pre-start.sh")
    if not pre_start.is_file() or "run-parts /var/lib/lxc/android/pre-start.d" not in pre_start.read_text():
        raise ValueError("The container pre-start script does not run the expected hooks")
    target = inside(root, DEST)
    if target.exists() and target.read_bytes() != RULE.read_bytes():
        raise ValueError("An existing tangorpro stub list differs; it has been left unchanged")
    return target


def stage(root, action):
    target = validate(root)
    if action == "check":
        return "Compatible startup files; workaround " + ("present" if target.exists() else "absent")
    if action == "remove":
        if target.exists():
            target.unlink()
        return "Workaround removed from the offline rootfs"
    data = RULE.read_bytes()
    if target.exists():
        return "Workaround already present; no changes needed"
    target.parent.mkdir(parents=True, exist_ok=True)
    # Create the completed rule beside its destination, then rename atomically.
    temporary = None
    try:
        with tempfile.NamedTemporaryFile(dir=target.parent, delete=False) as out:
            temporary = Path(out.name)
            out.write(data)
            out.flush()
            os.fsync(out.fileno())
        temporary.chmod(0o644)
        temporary.replace(target)
    finally:
        if temporary is not None and temporary.exists():
            temporary.unlink()
    return "Workaround staged; rule SHA-256: " + hashlib.sha256(data).hexdigest()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("action", choices=["check", "apply", "remove"])
    parser.add_argument("rootfs", type=Path)
    args = parser.parse_args()
    try:
        print(stage(args.rootfs, args.action))
    except (OSError, ValueError) as error:
        print(f"Refused: {error}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
