FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += " \
    file://add-support-for-tuna-device.patch \
    file://exclude-with-machine-public-quirks.patch"

