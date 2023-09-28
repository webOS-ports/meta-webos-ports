# Copyright (c) 2013-2023 LG Electronics, Inc.

inherit webos_qt_global

EXTENDPRAUTO:append = "webos113"

# Remove LGPL3-only files
python do_patch:append() {
    bb.build.exec_func('remove_LGPL3', d)
}

remove_LGPL3() {
    rm -vf ${S}/src/plugins/platforms/andr*oid/extract.cpp
}

# Disable features we don't use in all webOS products
# Needed in LuneOS
#PACKAGECONFIG_DEFAULT:remove = "dbus"


# Enable accessibility for qtquickcontrols
PACKAGECONFIG:append = " accessibility"

# Disable widget features
# Needed in LuneOS
#PACKAGECONFIG:remove = "widgets"

# Configure to use platform harfbuzz
PACKAGECONFIG:append = " harfbuzz"

# Configure to compile with GL ES2 instead of default desktop GL
PACKAGECONFIG_GRAPHICS = "gles2 eglfs"
PACKAGECONFIG_GRAPHICS:append:qemuall = " kms gbm"
PACKAGECONFIG_GRAPHICS:append:hammerhead = " kms gbm"
PACKAGECONFIG_GRAPHICS:append:pinephone = " kms gbm"
PACKAGECONFIG_GRAPHICS:append:pinephonepro = " kms gbm"
PACKAGECONFIG_GRAPHICS:append:pinetab2 = " kms gbm"
PACKAGECONFIG_GRAPHICS:append:tenderloin = " kms gbm"
PACKAGECONFIG_DISTRO += "sql-sqlite icu glib accessibility mtdev examples fontconfig xkbcommon"

# We had this enabled in our old gpro/meta-qt5 recipe
PACKAGECONFIG:append = " icu"
# We had this enabled in our old gpro/meta-qt5 recipe
PACKAGECONFIG:append = " glib"
# We had this enabled in our old gpro/meta-qt5 recipe
PACKAGECONFIG:append = " fontconfig"
# We had this enabled in our old gpro/meta-qt5 recipe
PACKAGECONFIG:append = " sql-sqlite"
# No longer added automatically
PACKAGECONFIG[gif] = "-DFEATURE_gif=ON,-DFEATURE_gif=OFF"
PACKAGECONFIG:append = " gif"
# Needed since qtwayland 5.12
PACKAGECONFIG:append:class-target = " xkbcommon"
# Disable loading text in image metadata
PACKAGECONFIG[no-imageio-text-loading] = "-DFEATURE_imageio_text_loading=OFF,-DFEATURE_imageio_text_loading=ON"
PACKAGECONFIG:append = " no-imageio-text-loading"

PACKAGECONFIG[linuxfb] = "-DFEATURE_linuxfb=ON,-DFEATURE_linuxfb=OFF"
PACKAGECONFIG:remove = "linuxfb"

PACKAGECONFIG[ico] = "-DFEATURE_ico=ON,-DFEATURE_ico=OFF"
PACKAGECONFIG:remove = "ico"

PACKAGECONFIG[sessionmanager] = "-DFEATURE_sessionmanager=ON,-DFEATURE_sessionmanager=OFF"
PACKAGECONFIG:remove = "sessionmanager"

PACKAGECONFIG[xlib] = "-DFEATURE_xlib=ON,-DFEATURE_xlib=OFF"
PACKAGECONFIG:remove = "xlib"

PACKAGECONFIG[eglfs-egldevice] = "-DFEATURE_eglfs_egldevice=ON,-DFEATURE_eglfs_egldevice=OFF"
PACKAGECONFIG:remove = "eglfs-egldevice"

PACKAGECONFIG[egl_x11] = "-DFEATURE_egl_x11=ON,-DFEATURE_egl_x11=OFF"
PACKAGECONFIG:remove = "egl_x11"

PACKAGECONFIG[system-sqlite] = "-DFEATURE_system_sqlite=ON,-DFEATURE_system_sqlite=OFF"
PACKAGECONFIG:append = " system-sqlite"

PACKAGECONFIG[system-pcre2] = "-DFEATURE_system_pcre2=ON,-DFEATURE_system_pcre2=OFF"
PACKAGECONFIG:remove = "system-pcre2"

PACKAGECONFIG:remove = "libinput"

# Depending on whether LTTNG support is enabled or not for the build we need to
# depend on the LTTNG providers to not let the build fail
inherit webos_lttng
# Disable lttng until wam eliminates dependency to qtbase (See PLAT-139794 for details)
#PACKAGECONFIG:append = "${@ ' lttng' if '${WEBOS_LTTNG_ENABLED}' == '1' else '' }"

# Do not build tests/ in webos
PACKAGECONFIG:remove = "tests"

# Need this flag as we don't build qtxxx-tools packages usually.
# See https://codereview.qt-project.org/c/qt/qtbase/+/452475.
EXTRA_OECMAKE:append = " -DQT_ALLOW_MISSING_TOOLS_PACKAGES=ON"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PATCHTOOL = "git"

# Upstream-Status: Inappropriate
# NOTE: Increase maxver when upgrading Qt version
SRC_URI:append = " \
    file://9901-Disable-Faux-bolding-in-Qts-FreeType-FontEngine.patch;maxver=6.5.3 \
"

# FIXME: Patches below can be dropped once all qmake-dependent components are switched to cmake.
# https://bugreports.qt.io/browse/WEBOSCI-66
# https://bugreports.qt.io/browse/WEBOSCI-81
SRC_URI:append:class-native = " file://9902-Revert-Remove-perl-related-functionality-from-CMake-_6.5.x.patch;minver=6.5.2;maxver=6.5.*"
SRC_URI:append:class-native = " file://9902-Revert-Remove-perl-related-functionality-from-CMake-_6.6.x.patch;minver=6.6.0"
# https://bugreports.qt.io/browse/WEBOSCI-73
SRC_URI:append = " file://9903-Revert-Remove-qmake-files-that-provide-support-for-b.patch;minver=6.5.1 "
# https://bugreports.qt.io/browse/WEBOSCI-76
SRC_URI:append = " file://9904-Revert-CMake-remove-tests-for-C-17-and-C11-and-earli.patch;minver=6.6.0"

# Flags needed for webOS
TARGET_CXXFLAGS:append = " \
    -DQFONTCACHE_MIN_COST=512 \
"

VIRTUAL-RUNTIME_gpu-libs ?= ""
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_gpu-libs}"
