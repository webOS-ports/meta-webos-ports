FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
           file://0001-Added-password-mask-delay.patch \
           file://0003-QtKeyExtensionGlobal-fix-export.patch \
           "

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
