FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGECONFIG = "udev mbim qrtr"

EXTRA_OEMESON += "\
                -Drmnet=true\
"
