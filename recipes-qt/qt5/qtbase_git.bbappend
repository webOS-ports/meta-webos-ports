PACKAGECONFIG_GL = "gles2"
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

# Needed to build libwayland_common_webos.a in qtwayland
WEBOS_NO_STATIC_LIBRARIES_WHITELIST = "libQt5PlatformSupport.a"

# Needed to build Qt5QmlDevTools in qtdeclarative
# | make[2]: *** No rule to make target `/home/mjansa/build-starfish-beehive-qt5.2/BUILD-m14tv/sysroots/m14tv/usr/lib/libQt5Bootstrap.a', needed by `../../lib/libQt5QmlDevTools.a'.  Stop.
WEBOS_NO_STATIC_LIBRARIES_WHITELIST += "libQt5Bootstrap.a"
