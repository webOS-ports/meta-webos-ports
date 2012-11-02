FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += "file://0001-Don-t-depend-on-not-existing-upstart-script-for-font.patch"

