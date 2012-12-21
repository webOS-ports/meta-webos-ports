FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 0}"

inherit webos-ports-submissions

SRCREV = "d0f842de2260e6e26e7af5b4e1f9051beab1a61f"
