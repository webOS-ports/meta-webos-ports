FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI_append = " file://0001-ucm-fix-handling-of-config-files-when-file-type-is-n.patch"
