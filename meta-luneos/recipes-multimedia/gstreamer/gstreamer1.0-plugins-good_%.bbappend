FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
PACKAGECONFIG:append = " libv4l2"

SRC_URI += "file://0001-gst_v4l2_fill_lists-abort-if-type-is-invalid.patch"

