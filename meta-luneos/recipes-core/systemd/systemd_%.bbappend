FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-rules-consider-MMC-device-partitions-with-partition-.patch \
    file://0002-fd_fdinfo_mnt_id-disable-fdinfo-stat.patch \
    file://0003-Disable-ProtectHome-and-ProtectSystem-for-old-kernel.patch \
"

RDEPENDS:${PN}:remove = "update-rc.d"

PACKAGECONFIG:remove = " \
    networkd    \
    resolved    \
    nss-resolve \
    timedated   \
    timesyncd   \
"

# By default systemd's Predictable Network Interface Names policy configured for qemu
# Currently we don't support this policy in qemu, so removing from systemd's configuration
do_install:append:qemuall() {
    rm -rf ${D}/${base_libdir}/systemd/network/99-default.link
}
