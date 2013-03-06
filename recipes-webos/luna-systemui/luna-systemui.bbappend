FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 9}"

inherit webos-ports-submissions

SRCREV = "1ed476a0862f11ab005990aa1ceefa8acd65a94b"
