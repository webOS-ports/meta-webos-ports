FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 0}"

inherit webos-ports-submissions

SRCREV = "d3195896851c9a37322abe1ea58eb55d29ffe5bb"
