FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI_append = " file://use-minimal-qpa.patch"

inherit webos-ports-submissions

SRCREV = "471ba9b80ec7e711b4b6d6c60e9a818c159054c0"
