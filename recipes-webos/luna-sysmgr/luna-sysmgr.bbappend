FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 8}"

SRC_URI_append = " \
    file://enable-webosports-first-use.patch \
    file://dont-spawn-bootanimation-process.patch"

inherit webos-ports-submissions

SRCREV = "040828a737d26673e960d0bf2d4eb4ec5f5c7b59"
