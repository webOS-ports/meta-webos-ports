DEPENDS += "mtdev"

PACKAGECONFIG_GL = "gles2"
PACKAGECONFIG_DISTRO = "sql-sqlite icu glib accessibility"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-HACK-Add-capatibility-to-have-QT_QPA_EGLFS_DEPTH-.-i.patch \
    file://0002-Enable-support-to-mtdev.patch \
    file://0003-Fixed-raw-coordinate-reporting-for-touch-events.patch \
    file://0004-Prevent-touch-coordinates-outside-target-geometry.patch \
    file://0005-Added-rotation-parameter-to-qevdevtouch.patch \
"

EXTRA_OECONF += "-qpa wayland-egl"
