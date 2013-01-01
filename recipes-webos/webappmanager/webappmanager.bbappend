FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

SRC_URI_append = " file://use-minimal-qpa.patch"

inherit webos-ports-submissions

SRCREV = "dd8afee44459cdea77d757f52decb53323035ccd"
