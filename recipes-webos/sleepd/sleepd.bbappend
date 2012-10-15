FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

RDEPENDS_${PN} = "wakelockd"

SRC_URI += "file://use-wakelockd-for-suspend.patch"
