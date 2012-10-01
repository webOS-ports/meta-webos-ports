FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

SRC_URI += " \
    file://exclude-with-machine-public-quirks.patch \
    file://dont-use-private-opengl-stuff.patch"

SRC_URI_append_tuna = " file://add-support-for-tuna-device.patch"
