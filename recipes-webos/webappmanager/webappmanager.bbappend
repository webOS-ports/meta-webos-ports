FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

SRC_URI_append = " file://use-minimal-qpa.patch"

inherit webos-ports-submissions

SRCREV = "f445f441be1d67231464b149e2070f3e9f5c24c8"
