FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# We're not yet using xkb for anything so disable it
XKB_DEPENDS = ""

SRC_URI = "git://gitorious.org/qt/qtwayland.git;protocol=git;branch=stable \
           file://0001-Try-one-more-option-to-get-a-valid-EGLDisplay-instan.patch \
           file://0002-Explicitly-state-precision-of-variables-in-shader.patch \
           file://0003-Don-t-disown-buffer-until-a-new-texture-id-is-reques.patch \
           file://0004-Revert-Integrate-QWindow-Visibility-into-Compositor-.patch \
           file://0005-Allow-key-events-to-be-delivered-to-unfocused-window.patch \
           file://0006-Add-an-extension-for-sending-keys-to-specific-window.patch \
           file://0007-Adjust-cast-for-wayland-EGL-API-change.patch \
"

SRCREV = "3e9412e2fd91e64a565ed8ddbef76f57ca9413d5"
