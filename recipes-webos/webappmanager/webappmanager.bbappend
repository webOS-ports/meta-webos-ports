FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

SRC_URI_append = " file://use-minimal-qpa.patch"

inherit webos-ports-submissions

SRCREV = "4e9d0b1b4533ae744e5fc2b6b431264766a1dc61"
