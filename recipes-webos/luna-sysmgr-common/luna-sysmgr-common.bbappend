FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 0}"

inherit webos-ports-submissions

SRCREV = "d9b1c51ff52aa576018bd04cb8b95d583e1cab16"
