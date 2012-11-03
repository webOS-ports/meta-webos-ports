FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

SRC_URI += "file://0001-When-running-install-make-target-install-header-file.patch"
