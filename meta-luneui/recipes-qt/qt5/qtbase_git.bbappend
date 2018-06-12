PACKAGECONFIG_GL = "gles2 eglfs"
PACKAGECONFIG_GL_append_qemuall = " kms gbm"
PACKAGECONFIG_DISTRO += "sql-sqlite icu glib accessibility mtdev examples fontconfig xkbcommon-evdev"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Determine-devicePixelRatio-from-environment-variable.patch \
"
SRC_URI_append_arm = " file://0002-Temporary-fix-for-SIGBUS-crash-disable-TLS.patch"
#SRC_URI_append_aarch64 = " file://0002-Temporary-fix-for-SIGBUS-crash-disable-TLS.patch"

QPA ?= "-qpa wayland-egl"
QPA_rpi = "-qpa wayland"
QPA_qemuall = "-qpa eglfs"

QT_CONFIG_FLAGS += "${QPA}"

do_install_append() {
    # Remove unwanted maliit input context plugin
    rm -f ${D}${libdir}/qt5/plugins/platforminputcontexts/libmaliitplatforminputcontextplugin.so
}
