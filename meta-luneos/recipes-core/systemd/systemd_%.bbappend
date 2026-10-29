FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-rules-consider-MMC-device-partitions-with-partition-.patch \
    file://0002-fd_fdinfo_mnt_id-disable-fdinfo-stat.patch \
    file://0001-systemd-oomd-depend-on-swap-on.patch \
    file://0002-Add-webos-interface.patch \
    file://0003-systemd-oomd-modify-oomd.conf.patch \
    file://0004-oomd-to-some.patch \
    file://0005-oomd-change-duration.patch \
    file://0006-Change-ownership-of-media-directory-to-support-non-r.patch \
"

# unposix_lock() has no fallback for a kernel without open file description
# locks (Linux 3.15), so on the 3.4 Halium machines systemd-sysusers cannot take
# the /etc/passwd lock. The arm64 Halium machines include bullhead-halium on 3.10,
# which has no OFD locks either; the fallback only runs where the kernel refuses
# F_OFD_SETLK, so the newer kernels are not affected.
SRC_URI:append:halium = " file://0007-lock-util-fall-back-to-POSIX-locks-without-OFD-support.patch"

# A kernel without fsopen() makes mount_option_supported() answer "cannot tell",
# which mount_procfs() reads as "supported", so units with ProtectProc=invisible
# fail where the proc mount rejects the textual hidepid= value (before 5.8): on
# 3.4, and on bullhead-halium's 3.10, where systemd-udevd, systemd-logind,
# systemd-userdbd and connman-vpn all failed with 226/NAMESPACE. A kernel with
# fsopen() (5.2 on) still gets a definite answer, so nothing changes there.
SRC_URI:append:halium = " file://0008-namespace-do-not-pass-textual-hidepid-without-fsopen.patch"

# pivot_root(".", ".") followed by umount2(".", MNT_DETACH) detaches the new root
# on kernels before 3.15 (3.4: the next mount() oopses; 3.10: units exit with
# 226/NAMESPACE). Every unit with a mount namespace hits that. Switch root with
# MS_MOVE and chroot() there; newer kernels keep pivot_root().
SRC_URI:append:halium = " file://0009-mount-util-switch-root-with-MS_MOVE-before-Linux-3.15.patch"

# ProtectHome=yes and ProtectSystem=yes used to be turned off on every machine
# for the old Halium kernels. Only the Halium machines run a kernel older than
# 3.15 (3.4: mako, flo, hammerhead, tenderloin, onyx, sm-t520; 3.10: bullhead,
# a3-2015, angler, athene, goyavewifi), but each Halium feed also carries newer
# kernels, so the patch decides on the running kernel: "yes" is off before 3.15
# only. Mainline machines take systemd's behaviour unchanged.
SRC_URI:append:halium = " file://0010-namespace-honour-ProtectHome-yes-and-ProtectSystem-yes-from-Linux-3.15.patch"

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
