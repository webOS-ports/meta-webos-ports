FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://0001-gdbus-codegen-replace-plus-also-with-underscore.patch \
"
