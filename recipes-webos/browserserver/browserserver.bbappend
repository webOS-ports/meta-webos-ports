FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += "file://0001-Don-t-depend-on-not-existing-upstart-script-for-font.patch"

inherit webos-ports-submissions

COMPONENT_NAME = "BrowserServer"

SRCREV = "dc6e53892b0b175457fcf4ce2a35a93bdb5d1d94"
