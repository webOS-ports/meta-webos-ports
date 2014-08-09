FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-bufferpool-Add-missing-error-checking-to-default_all.patch \
"

# Needed for gst-droid
EXTRA_OECONF += "--enable-check"
