FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

RDEPENDS_${PN}_append_tuna = " qt-webos-plugin"
RDEPENDS_${PN}_append_grouper = " qt-webos-plugin"

inherit webos-ports-submissions

SRCREV = "3fcd503d5b38999b7df96556ef8e7baeb2e7cd91"
