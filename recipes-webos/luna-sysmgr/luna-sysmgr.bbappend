FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

SRCREV = "b049e571e26d2b131bb0479fce63975cfb707146"
