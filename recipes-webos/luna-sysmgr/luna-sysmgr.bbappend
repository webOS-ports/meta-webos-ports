FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

SRCREV = "1bf5921e401a0eebbe3c2df0a443ac060ee62c05"
