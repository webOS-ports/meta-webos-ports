DEPENDS += "mtdev"

PACKAGECONFIG_GL = "gles2"
PACKAGECONFIG_DISTRO = "sql-sqlite icu glib accessibility"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-HACK-Add-capatibility-to-have-QT_QPA_EGLFS_DEPTH-.-i.patch \
    file://0002-Enable-support-to-mtdev.patch \
"

EXTRA_OECONF += "-qpa wayland-egl"
