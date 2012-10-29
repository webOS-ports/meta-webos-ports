FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

SRC_URI += "file://install-sleepd-conf-correctly.patch"
