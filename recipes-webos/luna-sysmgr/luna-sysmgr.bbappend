FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 7}"

SRC_URI_append = " \
    file://disable-start-of-firstuse-app.patch \
    file://dont-spawn-bootanimation-process.patch"

inherit webos-ports-submissions

SRCREV = "0b0e64d18f149a8ee50261a4048e9d30e11241a1"
