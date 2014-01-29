FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# We're not yet using xkb for anything so disable it
XKB_DEPENDS = ""

SRC_URI = "git://gitorious.org/qt/qtwayland.git;protocol=git;branch=stable \
           file://0001-Try-one-more-option-to-get-a-valid-EGLDisplay-instan.patch \
           file://0002-Explicitly-state-precision-of-variables-in-shader.patch \
           file://0003-Don-t-disown-buffer-until-a-new-texture-id-is-reques.patch \
"
# only for our ARM MACHINEs with different EGL headers
SRC_URI_append_arm = " file://0004-Adjust-cast-for-wayland-EGL-API-change.patch"

SRCREV = "3209f61310cb5244704e7cfa4f16044420c96747"

FILES_${PN} += "${libdir}/qt5/plugins/wayland-graphics-integration"
FILES_${PN}-dbg += "${libdir}/qt5/plugins/wayland-graphics-integration/*/.debug"
