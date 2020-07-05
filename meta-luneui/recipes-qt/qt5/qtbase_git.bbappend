PACKAGECONFIG_GL = "gles2 eglfs"
PACKAGECONFIG_GL_append_qemuall = " kms gbm"
PACKAGECONFIG_GL_append_pinephone = " kms gbm"
PACKAGECONFIG_DISTRO += "sql-sqlite icu glib accessibility mtdev examples fontconfig xkbcommon"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Determine-devicePixelRatio-from-environment-variable.patch \
"

# Fix a crash on armv7 devices. A similar commit (https://github.com/qt/qtbase/commit/78665d8a0cc06aa17a0dc3987afb6d2f3d86e6af) has been since reverted in Qt due to other issues, but this fixes
# annoying crashes (std::bad_alloc) on Qt apps in LuneOS for old armv7 devices.

SRC_URI_append_halium_armv7a += " \
    file://0002-QThreadData-use-libpthread-instead-of-gcc-TLS.patch \
"

QPA ?= "-qpa wayland-egl"
QPA_rpi = "-qpa wayland"
QPA_qemuall = "-qpa eglfs"

QT_CONFIG_FLAGS += "${QPA}"

do_install_append() {
    # Remove unwanted maliit input context plugin
    rm -f ${D}${libdir}/qt5/plugins/platforminputcontexts/libmaliitplatforminputcontextplugin.so
}
