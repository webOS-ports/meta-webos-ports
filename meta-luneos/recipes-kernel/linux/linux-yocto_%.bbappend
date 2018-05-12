FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += " \
    file://defconfig \
    file://squashfs.cfg \
    file://video.cfg \
"
