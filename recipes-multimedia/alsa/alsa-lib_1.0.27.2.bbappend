FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
    file://0001-ucm-fix-handling-of-config-files-when-file-type-is-n.patch \
"
