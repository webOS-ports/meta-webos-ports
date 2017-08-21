PACKAGECONFIG_GL = "gles2 eglfs kms gbm"
PACKAGECONFIG_DISTRO += "sql-sqlite icu glib accessibility mtdev examples fontconfig xkbcommon-evdev"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Determine-devicePixelRatio-from-environment-variable.patch \
    file://0002-Add-needsWorkaround-for-additional-Adreno-targets.patch \
"

do_install_append() {
    # Remove unwanted maliit input context plugin
    rm -f ${D}${libdir}/qt5/plugins/platforminputcontexts/libmaliitplatforminputcontextplugin.so
}
