FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

COMPONENT_NAME = "isis-browser"
SRCREV = "57f91972e6d1abc8c32e476920e1683d026b4d3e"
