FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += "file://0001-Don-t-depend-on-not-existing-upstart-script-for-font.patch"

inherit webos-ports-submissions

WEBOS_PORTS_REPO_NAME = "BrowserServer"

SRCREV = "58ec890291dfa1a927faba069f8b0f1d8ac94e2d"
