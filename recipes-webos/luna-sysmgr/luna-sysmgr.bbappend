FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

SRCREV = "cb58d1d2a78a0a9076610718d6c03915c4c304ce"
