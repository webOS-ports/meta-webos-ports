FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

SRCREV = "8d4d08fd716cabc6fe8d8e65ed445df34f0a4cd5"
