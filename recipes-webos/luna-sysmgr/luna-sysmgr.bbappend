FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 8}"

SRC_URI_append = " \
    file://enable-webosports-first-use.patch \
    file://dont-spawn-bootanimation-process.patch"

inherit webos-ports-submissions

SRCREV = "32bdd6045e57f81c5130419d193d22f504b667ab"
