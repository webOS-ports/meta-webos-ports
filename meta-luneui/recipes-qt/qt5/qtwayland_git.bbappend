FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PACKAGECONFIG[wayland-brcm] = "-feature-wayland-brcm,-no-feature-wayland-brcm,virtual/egl"
#PACKAGECONFIG[drm-egl-server] = "-feature-drm-egl-server,-no-feature-drm-egl-server,libdrm virtual/egl"
#PACKAGECONFIG[libhybris-egl-server] = "-feature-libhybris-egl-server,-no-feature-libhybris-egl-server,libhybris"

EXTRA_PACKAGECONFIG ?= ""
EXTRA_PACKAGECONFIG_qemuall = ""
EXTRA_PACKAGECONFIG_hammerhead = ""
EXTRA_PACKAGECONFIG_pinephone = ""
EXTRA_PACKAGECONFIG_rpi = ""
#EXTRA_PACKAGECONFIG_rpi = "wayland-brcm"
PACKAGECONFIG_append_class-target = " ${EXTRA_PACKAGECONFIG}"

SRC_URI += " \
    file://0001-Fix-QtKeyExtensionGlobal-s-export.patch \
    file://0002-Revert-Remove-QWaylandExtendedSurface-from-the-priva.patch \
    file://0003-QWaylandXdgSurface-handle-ExtendedSurface-window-pro.patch \
"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
