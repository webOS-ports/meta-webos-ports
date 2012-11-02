FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += " \
    file://0001-When-running-install-make-target-install-header-file.patch \
    file://0002-Create-a-shared-library-instead-a-static-one.patch"
