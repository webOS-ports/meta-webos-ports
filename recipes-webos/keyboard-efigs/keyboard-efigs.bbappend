FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 0}"

inherit webos-ports-submissions

SRCREV = "3630dff15ad626c622d199cf52721e67399fe44e"
