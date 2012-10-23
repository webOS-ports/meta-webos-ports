FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 3}"

RDEPENDS_${PN} = "wakelockd"

SRC_URI += " \
    file://use-wakelockd-for-suspend.patch \
    file://install-sleepd-conf-correctly.patch"
