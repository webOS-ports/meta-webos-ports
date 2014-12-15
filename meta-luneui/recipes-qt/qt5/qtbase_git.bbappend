PACKAGECONFIG_GL = "gl"
PACKAGECONFIG_X11 = ""
PACKAGECONFIG_DISTRO = "sql-sqlite icu glib accessibility mtdev"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Determine-devicePixelRatio-from-environment-variable.patch \
    file://0002-qfont-Don-t-crash-when-platform-integration-isn-t-av.patch \
"

EXTRA_OECONF += "-qpa wayland-egl"

do_install_append() {
    # Remove unwanted maliit input context plugin
    rm -f ${D}${libdir}/qt5/plugins/platforminputcontexts/libmaliitplatforminputcontextplugin.so
}
