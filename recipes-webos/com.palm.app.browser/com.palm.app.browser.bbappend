FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

COMPONENT_NAME = "isis-browser"
SRCREV = "37906c2738e1920fcc1e80ec3a78f157413322c3"
