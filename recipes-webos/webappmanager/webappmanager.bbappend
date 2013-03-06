FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

inherit webos-ports-submissions

SRCREV = "3fcd503d5b38999b7df96556ef8e7baeb2e7cd91"
