# Fail the image when its root filesystem has unsafe permissions:
#
#  - a world-writable file under /usr, /etc, /lib, /bin or /sbin;
#  - a world-writable directory without the sticky bit, anywhere;
#  - a setuid or setgid file that is not in LUNEOS_IMAGE_QA_SUID_ALLOW.
#
# A world-writable file elsewhere (/var, /media, ...) is a warning.
#
# The check runs as the last ROOTFS_POSTPROCESS_COMMAND, inside do_rootfs. That
# is a fakeroot task, so the modes and owners it reads come from pseudo and are
# the ones the image will have. The logic lives in lib/webos/image_qa.py, which
# has unit tests in lib/webos/tests/test_image_qa.py.

# Setuid/setgid files the image may carry. fnmatch patterns on the path in the
# image. Keep this list short: everything on it runs as root for any user.
LUNEOS_IMAGE_QA_SUID_ALLOW ?= " \
    ${bindir}/busybox.suid \
    ${bindir}/chage \
    ${bindir}/expiry \
    ${bindir}/fusermount \
    ${bindir}/gpasswd \
    ${bindir}/mount.util-linux \
    ${bindir}/umount.util-linux \
    ${bindir}/newuidmap \
    ${bindir}/newgidmap \
    ${bindir}/newgrp.shadow \
    ${bindir}/passwd.shadow \
    ${base_bindir}/su.shadow \
    ${libexecdir}/dbus-daemon-launch-helper \
    ${base_sbindir}/unix_chkpwd \
"

# World-writable directories that may lack the sticky bit. Empty on purpose.
LUNEOS_IMAGE_QA_WW_DIR_ALLOW ?= ""

# World-writable files that do not even deserve a warning.
LUNEOS_IMAGE_QA_WW_FILE_ALLOW ?= ""

# Top-level directories in which a world-writable file is an error.
LUNEOS_IMAGE_QA_SYSTEM_DIRS ?= "usr etc lib lib32 lib64 bin sbin"

# :append so that it runs after every other postprocess command, including
# those the image recipes add with +=.
ROOTFS_POSTPROCESS_COMMAND:append = " luneos_image_qa;"

python luneos_image_qa () {
    # Called by their full names, so that bitbake's signature of this task
    # follows changes to the module functions.
    errors, warnings = webos.image_qa.scan(
        d.getVar("IMAGE_ROOTFS"),
        system_dirs=(d.getVar("LUNEOS_IMAGE_QA_SYSTEM_DIRS") or "").split(),
        suid_allow=(d.getVar("LUNEOS_IMAGE_QA_SUID_ALLOW") or "").split(),
        ww_dir_allow=(d.getVar("LUNEOS_IMAGE_QA_WW_DIR_ALLOW") or "").split(),
        ww_file_allow=(d.getVar("LUNEOS_IMAGE_QA_WW_FILE_ALLOW") or "").split())

    if warnings:
        bb.warn("luneos-image-qa: world-writable files in %s:\n%s"
                % (d.getVar("PN"), webos.image_qa.format_report(warnings)))
    if errors:
        bb.fatal("luneos-image-qa: unsafe permissions in %s:\n%s\n"
                 "Fix the recipe that installs them; extend "
                 "LUNEOS_IMAGE_QA_SUID_ALLOW or LUNEOS_IMAGE_QA_WW_DIR_ALLOW "
                 "only for a file that really needs it."
                 % (d.getVar("PN"), webos.image_qa.format_report(errors)))
    bb.note("luneos-image-qa: %d warnings, no errors" % len(warnings))
}
luneos_image_qa[vardeps] += "LUNEOS_IMAGE_QA_SUID_ALLOW LUNEOS_IMAGE_QA_WW_DIR_ALLOW \
    LUNEOS_IMAGE_QA_WW_FILE_ALLOW LUNEOS_IMAGE_QA_SYSTEM_DIRS"
