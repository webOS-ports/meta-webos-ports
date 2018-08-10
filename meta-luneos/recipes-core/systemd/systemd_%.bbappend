FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Create-disk-by-partlabel-links-for-mmcblk-partitions.patch \
    file://0002-fd_fdinfo_mnt_id-disable-fdinfo-stat.patch \
    file://0003-Disable-ProtectHome-and-ProtectSystem-for-old-kernel.patch \
"
