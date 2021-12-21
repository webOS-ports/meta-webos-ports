FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGECONFIG[wayland-brcm] = "-feature-wayland-brcm,-no-feature-wayland-brcm,virtual/egl"
#PACKAGECONFIG[drm-egl-server] = "-feature-drm-egl-server,-no-feature-drm-egl-server,libdrm virtual/egl"
#PACKAGECONFIG[libhybris-egl-server] = "-feature-libhybris-egl-server,-no-feature-libhybris-egl-server,libhybris"

EXTRA_PACKAGECONFIG ?= ""
EXTRA_PACKAGECONFIG:qemuall = ""
EXTRA_PACKAGECONFIG:hammerhead = ""
EXTRA_PACKAGECONFIG:pinephone = ""
EXTRA_PACKAGECONFIG:pinephonepro = ""
EXTRA_PACKAGECONFIG:rpi = ""
#EXTRA_PACKAGECONFIG:rpi = "wayland-brcm"
PACKAGECONFIG:append:class-target = " ${EXTRA_PACKAGECONFIG}"

SRC_URI += " \
    file://0001-Fix-QtKeyExtensionGlobal-s-export.patch \
    file://0002-Revert-Remove-QWaylandExtendedSurface-from-the-priva.patch \
    file://0003-QWaylandXdgSurface-handle-ExtendedSurface-window-pro.patch \
"

FILES:${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
