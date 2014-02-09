DEPENDS += "mtdev"

PACKAGECONFIG_GL = "gles2"
PACKAGECONFIG_DISTRO = "sql-sqlite icu glib accessibility"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-HACK-Add-capatibility-to-have-QT_QPA_EGLFS_DEPTH-.-i.patch \
    file://0002-Enable-support-to-mtdev.patch \
    file://0003-Don-t-crash-when-platform-integration-isn-t-availabl.patch \
    file://0001-Make-wayland-scanner-install-generated-headers.patch \
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
