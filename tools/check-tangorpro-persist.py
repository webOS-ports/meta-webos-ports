#!/usr/bin/python3
"""Read-only diagnostic for tangorpro's persistent storage on LuneOS.

mount-android.sh must first mount the vendor's separate fstab.persist. This
guard never mounts, formats, repairs, clears, or writes secure-storage data.
See https://github.com/webOS-ports/meta-webos-ports/issues/822.
"""

import os
import stat
import sys


class PersistError(Exception):
    """The persist configuration does not match the stock storage layout."""


def codename(android_root, device_tree="/proc/device-tree/compatible"):
    # getprop is not available until after this pre-start check has run.
    properties = {}
    try:
        with open(os.path.join(android_root, "vendor/build.prop")) as source:
            for line in source:
                key, separator, value = line.strip().partition("=")
                if separator:
                    properties[key] = value
    except OSError:
        pass
    for key in ("ro.product.vendor.device", "ro.vendor.product.device", "ro.product.device"):
        if properties.get(key):
            return properties[key]
    try:
        with open(device_tree, "rb") as source:
            compatible = source.read().split(b"\0", 1)[0].decode("ascii")
        if "," in compatible:
            return compatible.split(",", 1)[1]
    except (OSError, UnicodeError):
        pass
    return "unknown"


def container_shares_mnt(config):
    # The last mount at mnt wins; an earlier correct entry must not hide a
    # later one that covers it with a different source or drops submounts.
    shared = False
    with open(config) as source:
        for line in source:
            key, separator, value = line.split("#", 1)[0].partition("=")
            if not separator or key.strip() != "lxc.mount.entry":
                continue
            fields = value.split()
            if len(fields) >= 4 and fields[1] in ("mnt", "/mnt"):
                options = set(fields[3].split(","))
                shared = (fields[0] == "/mnt" and fields[2] in ("none", "bind")
                          and "rbind" in options and "ro" not in options)
    return shared


def check_persist(mounts="/proc/self/mounts", target="/mnt/vendor/persist",
                  device_dirs=("/dev/disk/by-partlabel", "/dev/disk/by-name", "/dev/block/by-name"),
                  config="/var/lib/lxc/android/config", stat_fn=os.stat):
    entry = None
    with open(mounts) as source:
        for line in source:
            fields = line.split()
            if len(fields) >= 4 and fields[1] == target:
                entry = fields
    if entry is None:
        raise PersistError(f"{target} is not mounted; check vendor/etc/fstab.persist and mount-android.sh")
    source, _, fstype, flags = entry[:4]
    if fstype not in ("ext4", "f2fs"):
        raise PersistError(f"{target} is {fstype}, not the vendor's ext4/f2fs persist filesystem")
    if "rw" not in flags.split(",") or "ro" in flags.split(","):
        raise PersistError(f"{target} is read-only; Trusty needs its existing writable persist tier")

    expected = None
    expected_path = None
    for directory in device_dirs:
        candidate = os.path.join(directory, "persist")
        try:
            info = stat_fn(candidate)
        except FileNotFoundError:
            continue
        if stat.S_ISBLK(info.st_mode):
            expected, expected_path = info.st_rdev, candidate
            break
    if expected is None:
        raise PersistError("cannot identify the persist block device; refusing to trust a directory")
    # Compare device numbers, not path strings: /dev/block/sda1 and /dev/sda1
    # can be distinct nodes referring to the same block device.
    actual = stat_fn(source)
    if not stat.S_ISBLK(actual.st_mode) or actual.st_rdev != expected:
        raise PersistError(f"{target} is not backed by {expected_path}")
    if not container_shares_mnt(config):
        raise PersistError("LXC must recursively bind the writable host /mnt into the container")
    return f"verified writable {fstype} persist ({expected_path}); LXC shares it via /mnt"


def main():
    device = codename(os.environ.get("ANDROID_ROOT", "/android"))
    if device != "tangorpro":
        return 0
    try:
        message = check_persist()
    except (OSError, PersistError) as error:
        print(f"tangorpro-check-persist: ERROR: {error}", file=sys.stderr)
        return 1
    print(f"tangorpro-check-persist: {message}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
