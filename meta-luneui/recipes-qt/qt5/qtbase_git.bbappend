PACKAGECONFIG_GL = "gles2 eglfs"
PACKAGECONFIG_GL:append:qemuall = " kms gbm"
PACKAGECONFIG_GL:append:pinephone = " kms gbm"
PACKAGECONFIG_DISTRO += "sql-sqlite icu glib accessibility mtdev examples fontconfig xkbcommon"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Determine-devicePixelRatio-from-environment-variable.patch \
"

# Fix a crash on armv7 devices. A similar commit (https://github.com/qt/qtbase/commit/78665d8a0cc06aa17a0dc3987afb6d2f3d86e6af) has been since reverted in Qt due to other issues, but this fixes
# annoying crashes (std::bad_alloc) on Qt apps in LuneOS for old armv7 devices.

SRC_URI:append_halium:armv7a += " \
    file://0002-QThreadData-use-libpthread-instead-of-gcc-TLS.patch \
"

QPA ?= "-qpa wayland-egl"
QPA:rpi = "-qpa wayland"
QPA:qemuall = "-qpa eglfs"

QT_CONFIG_FLAGS += "${QPA}"

do_install:append() {
    # Remove unwanted maliit input context plugin
    rm -f ${D}${libdir}/qt5/plugins/platforminputcontexts/libmaliitplatforminputcontextplugin.so
}
