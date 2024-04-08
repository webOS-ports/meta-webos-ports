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
