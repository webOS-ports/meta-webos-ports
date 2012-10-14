FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

RDEPENDS = "wakelockd"

SRC_URI += "file://use-wakelockd-for-suspend.patch"
