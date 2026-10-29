# Filesystem permission checks for LuneOS image root filesystems.
#
# Used by luneos-image-qa.bbclass from ROOTFS_POSTPROCESS_COMMAND. do_rootfs
# is a fakeroot task, so os.lstat() here goes through pseudo and returns the
# owners and modes the image will really have, not those of the build user.
#
# Kept free of bitbake imports so that it can be tested on its own
# (see meta-luneos/lib/webos/tests/test_image_qa.py).
#
# SPDX-License-Identifier: MIT

import fnmatch
import os
import stat

# Top-level directories whose files must never be world-writable: they hold
# what root executes and reads its configuration from.
DEFAULT_SYSTEM_DIRS = ("usr", "etc", "lib", "lib32", "lib64", "bin", "sbin")


def _norm(path):
    """'/usr/bin/x', 'usr/bin/x' and './usr/bin/x' all become '/usr/bin/x'."""
    path = path.strip()
    if path.startswith("./"):
        path = path[1:]
    if not path.startswith("/"):
        path = "/" + path
    return path


def _allowed(path, patterns):
    return any(fnmatch.fnmatchcase(path, p) for p in patterns)


def _mode_str(st):
    return "%04o" % stat.S_IMODE(st.st_mode)


def scan(rootfs, system_dirs=DEFAULT_SYSTEM_DIRS, suid_allow=(),
         ww_dir_allow=(), ww_file_allow=()):
    """Walk an image root filesystem without following symlinks.

    Returns (errors, warnings), each a sorted list of (path, mode, reason)
    with path relative to the image root ("/usr/bin/foo"):

    errors:
      - a world-writable regular file (or other non-directory, non-symlink)
        under one of system_dirs;
      - a world-writable directory without the sticky bit, anywhere, unless
        it matches ww_dir_allow;
      - a setuid or setgid file that does not match suid_allow.
    warnings:
      - a world-writable file outside system_dirs, unless it matches
        ww_file_allow.

    The allow lists take fnmatch patterns on the normalised path.
    """
    system_dirs = tuple(d.strip("/") for d in system_dirs if d.strip("/"))
    suid_allow = [_norm(p) for p in suid_allow]
    ww_dir_allow = [_norm(p) for p in ww_dir_allow]
    ww_file_allow = [_norm(p) for p in ww_file_allow]

    errors = []
    warnings = []
    rootfs = os.path.abspath(rootfs)

    def check(full, rel):
        try:
            st = os.lstat(full)
        except FileNotFoundError:
            return
        mode = st.st_mode
        if stat.S_ISLNK(mode):
            return
        perm = stat.S_IMODE(mode)
        top = rel.lstrip("/").split("/", 1)[0]

        if stat.S_ISDIR(mode):
            if perm & stat.S_IWOTH and not perm & stat.S_ISVTX \
                    and not _allowed(rel, ww_dir_allow):
                errors.append((rel, _mode_str(st),
                               "world-writable directory without the sticky bit"))
            return

        if perm & stat.S_IWOTH and not _allowed(rel, ww_file_allow):
            if top in system_dirs:
                errors.append((rel, _mode_str(st),
                               "world-writable file under /%s" % top))
            else:
                warnings.append((rel, _mode_str(st), "world-writable file"))

        if perm & (stat.S_ISUID | stat.S_ISGID) and stat.S_ISREG(mode) \
                and not _allowed(rel, suid_allow):
            kind = []
            if perm & stat.S_ISUID:
                kind.append("setuid")
            # setgid without group execute is mandatory locking, not a
            # privilege; only report it when it grants the group.
            if perm & stat.S_ISGID and perm & stat.S_IXGRP:
                kind.append("setgid")
            if kind:
                errors.append((rel, _mode_str(st),
                               "%s, not in the allow list" % "/".join(kind)))

    check(rootfs, "/")
    for dirpath, dirnames, filenames in os.walk(rootfs, followlinks=False):
        reldir = "/" + os.path.relpath(dirpath, rootfs)
        if reldir == "/.":
            reldir = ""
        # os.walk lists symlinks to directories in dirnames but does not
        # descend into them; check() skips them as symlinks.
        for name in dirnames + filenames:
            check(os.path.join(dirpath, name), reldir + "/" + name)

    return sorted(errors), sorted(warnings)


def format_report(entries):
    return "\n".join("  %s %s: %s" % (mode, path, reason)
                     for path, mode, reason in entries)
