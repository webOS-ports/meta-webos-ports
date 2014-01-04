DEPENDS += "mtdev"

PACKAGECONFIG_GL = "gles2"
PACKAGECONFIG_DISTRO = "sql-sqlite icu glib accessibility"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-HACK-Add-capatibility-to-have-QT_QPA_EGLFS_DEPTH-.-i.patch \
    file://0002-Enable-support-to-mtdev.patch \
    file://0003-Don-t-crash-when-platform-integration-isn-t-availabl.patch \
"

EXTRA_OECONF += "-qpa wayland-egl"

do_install_append() {
    # Remove unwanted maliit input context plugin
    rm -f ${D}${libdir}/qt5/plugins/platforminputcontexts/libmaliitplatforminputcontextplugin.so
}
