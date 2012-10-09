FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 3}"

inherit webos-ports-submissions

SRCREV = "f5d767356d53ecae27de6e7f4476fc949d36022e"
