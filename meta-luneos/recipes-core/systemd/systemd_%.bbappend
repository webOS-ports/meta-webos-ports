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
    cgroupv2 \
    coredump \
    elfutils \
"

FILES:${PN} += "${datadir}/dbus-1/system.d/com.webos.MemoryManager1.conf"


# By default systemd's Predictable Network Interface Names policy configured for qemu
# Currently we don't support this policy in qemu, so removing from systemd's configuration
do_install:append:qemuall() {
    rm -rf ${D}/${base_libdir}/systemd/network/99-default.link
}

PR:append = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'smack4', '', d)}"
PATCH_SMACK = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', '\
    file://0001-SMACK-add-loading-unconfined-label.patch \
    file://0001-meson-add-smack-default-process-label-option.patch \
    file://0001-fileio.c-fix-build-with-smack-enabled.patch \
', '', d)}"

SRC_URI:append = " ${PATCH_SMACK}"

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
