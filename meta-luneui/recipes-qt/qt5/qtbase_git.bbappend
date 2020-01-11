PACKAGECONFIG_GL = "gles2 eglfs"
PACKAGECONFIG_GL_append_qemuall = " kms gbm"
PACKAGECONFIG_GL_append_pinephone = " kms gbm"
PACKAGECONFIG_DISTRO += "sql-sqlite icu glib accessibility mtdev examples fontconfig xkbcommon"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Determine-devicePixelRatio-from-environment-variable.patch \
"

QPA ?= "-qpa wayland-egl"
QPA_rpi = "-qpa wayland"
QPA_qemuall = "-qpa eglfs"

QT_CONFIG_FLAGS += "${QPA}"

do_install_append() {
    # Remove unwanted maliit input context plugin
    rm -f ${D}${libdir}/qt5/plugins/platforminputcontexts/libmaliitplatforminputcontextplugin.so
}
