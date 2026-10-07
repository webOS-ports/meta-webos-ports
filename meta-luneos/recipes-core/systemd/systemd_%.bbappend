FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-rules-consider-MMC-device-partitions-with-partition-.patch \
    file://0002-fd_fdinfo_mnt_id-disable-fdinfo-stat.patch \
    file://0003-Disable-ProtectHome-and-ProtectSystem-for-old-kernel.patch \
    file://0001-systemd-oomd-depend-on-swap-on.patch \
    file://0002-Add-webos-interface.patch \
    file://0003-systemd-oomd-modify-oomd.conf.patch \
    file://0004-oomd-to-some.patch \
    file://0005-oomd-change-duration.patch \
    file://0006-Change-ownership-of-media-directory-to-support-non-r.patch \
"

# unposix_lock() has no fallback for a kernel without open file description
# locks (Linux 3.15), so on the 3.4 Halium machines systemd-sysusers cannot take
# the /etc/passwd lock. Only the machines on those kernels need the patch.
SRC_URI:append:armv7a:halium = " file://0007-lock-util-fall-back-to-POSIX-locks-without-OFD-support.patch"

# A kernel without fsopen() makes mount_option_supported() answer "cannot tell",
# which mount_procfs() reads as "supported", so units with ProtectProc=invisible
# fail on 3.4 where the proc mount rejects the textual hidepid= value.
SRC_URI:append:armv7a:halium = " file://0008-namespace-do-not-pass-textual-hidepid-without-fsopen.patch"

# pivot_root(".", ".") followed by umount2(".", MNT_DETACH) detaches the new root
# on a 3.4 kernel, and the next mount() on it oopses the kernel. Every unit with
# a mount namespace hits that. Always switch root with MS_MOVE and chroot().
SRC_URI:append:armv7a:halium = " file://0009-mount-util-always-switch-root-with-MS_MOVE.patch"

RDEPENDS:${PN}:remove = "update-rc.d"

PACKAGECONFIG:remove = " \
    networkd    \
    resolved    \
    nss-resolve \
    timedated   \
    timesyncd   \
"
PACKAGECONFIG:append = " \
    oomd \
    coredump \
    elfutils \
"

FILES:${PN} += "${datadir}/dbus-1/system.d/com.webos.MemoryManager1.conf"

# By default systemd's Predictable Network Interface Names policy configured for qemu
# Currently we don't support this policy in qemu, so removing from systemd's configuration
do_install:append:qemuall() {
    rm -rf ${D}/${base_libdir}/systemd/network/99-default.link
}

SYSTEMD_SMACK_RUN_LABEL = "System"
SYSTEMD_SMACK_DEFAULT_PROCESS_LABEL = "System::Run"

EXTRA_OEMESON_SMACK = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', '\
    -Dsmack-run-label=${SYSTEMD_SMACK_RUN_LABEL} \
    -Dsmack-default-process-label=${SYSTEMD_SMACK_DEFAULT_PROCESS_LABEL} \
', '', d)}"

EXTRA_OEMESON:append = " ${EXTRA_OEMESON_SMACK}"

do_install[postfuncs] += "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'set_tmp_star', '', d)}"

set_tmp_star () {
    tmpmount="${D}/${systemd_unitdir}/system/tmp.mount"
    if [ -f "$tmpmount" ]; then
        sed -i -e 's/^Options=/Options=smackfsroot=*,/' "$tmpmount"
    fi
}
