FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://gitorious.org/qt/qtwayland.git;protocol=git;branch=stable \
           file://0001-Try-one-more-option-to-get-a-valid-EGLDisplay-instan.patch \
           file://0002-Explicitly-state-precision-of-variables-in-shader.patch \
           file://0003-Don-t-disown-buffer-until-a-new-texture-id-is-reques.patch \
           file://0001-Don-t-use-internal-wayland-based-input-context.patch \
           file://0001-Fix-the-client-behavior-when-showing-or-hiding-a-surface.patch \
"

SRCREV = "3209f61310cb5244704e7cfa4f16044420c96747"

FILES_${PN} += "${libdir}/qt5/plugins/wayland-graphics-integration"
FILES_${PN}-dbg += "${libdir}/qt5/plugins/wayland-graphics-integration/*/.debug"
