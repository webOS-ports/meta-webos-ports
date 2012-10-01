FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 3}"

SRC_URI += " \
    file://add-support-for-tuna-device.patch \
    file://exclude-with-machine-public-quirks.patch \
    file://dont-use-private-opengl-stuff.patch"

