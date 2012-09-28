FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

SRC_URI += "file://add-support-for-tuna-machine.patch"
