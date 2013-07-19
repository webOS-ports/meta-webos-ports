FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# We're not yet using xkb for anything so disable it
XKB_DEPENDS = ""

SRC_URI = "git://gitorious.org/qt/qtwayland.git;protocol=git;branch=master \
           file://0001-Try-one-more-option-to-get-a-valid-EGLDisplay-instan.patch \
           file://0002-Explicitly-state-precision-of-variables-in-shader.patch \
           file://0003-Don-t-disown-buffer-until-a-new-texture-id-is-reques.patch \
           file://0004-Revert-Integrate-QWindow-Visibility-into-Compositor-.patch"

SRCREV = "e8e5457165d6797b2a2d33a73c9b362e82688459"
