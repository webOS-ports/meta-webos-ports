FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI_append = " file://use-minimal-qpa.patch"

inherit webos-ports-submissions

SRCREV = "b7cd3c5919114f14f74358ba2021b8ccedc5a144"
