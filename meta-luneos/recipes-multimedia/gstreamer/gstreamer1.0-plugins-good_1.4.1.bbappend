FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Set-specific-media.role-for-pulsesink-probe.patch \
    file://0002-qtmux-write-rotation-information-into-the-TKHD-matri.patch \
"
