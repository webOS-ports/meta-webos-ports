FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += "file://add-common-linux-target.patch"

EXTRA_OECMAKE += "-DTARGET:STRING=common"
