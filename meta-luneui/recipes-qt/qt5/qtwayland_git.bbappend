FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Added-password-mask-delay.patch \
    file://0002-Fix-QtKeyExtensionGlobal-s-export.patch \
    file://0003-Revert-Remove-QWaylandExtendedSurface-from-the-priva.patch \
"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
