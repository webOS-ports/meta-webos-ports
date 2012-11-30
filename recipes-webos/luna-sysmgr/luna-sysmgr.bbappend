FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 5}"

SRC_URI += "file://disable-start-of-firstuse-app.patch"

inherit webos-ports-submissions

SRCREV = "40cfa484ff4dd8dd1cc4b26100e4b9855057c252"
