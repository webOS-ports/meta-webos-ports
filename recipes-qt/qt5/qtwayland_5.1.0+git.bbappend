FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# We're not yet using xkb for anything so disable it
XKB_DEPENDS = ""

SRC_URI = "git://gitorious.org/qt/qtwayland.git;protocol=git;branch=master \
           file://0001-Try-one-more-option-to-get-a-valid-EGLDisplay-instan.patch \
           file://0002-Explicitly-state-precision-of-variables-in-shader.patch \
           file://0003-Handle-connection-error-by-exiting-the-application.patch"

SRCREV = "5082ea72c10c4e88d96c2869d9bed06dcd33b247"
