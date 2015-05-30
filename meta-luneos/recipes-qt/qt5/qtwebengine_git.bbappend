FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-git:"

LIC_FILES_CHKSUM = " \
    file://src/core/browser_context_qt.cpp;md5=5fe719c44250955a5d5f8fb15fc8b1da;beginline=1;endline=35 \
    file://src/3rdparty/chromium/LICENSE;md5=537e0b52077bf0a616d0a0c8a79bc9d5 \
    file://LICENSE.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02 \
"

DEPENDS += "qtwebchannel"

QT_MODULE_BRANCH = "dev"
QT_MODULE_BRANCH_CHROMIUM = "40.0.2214-based"

SRC_URI = " \
    ${QT_GIT}/qt/${QT_MODULE}.git;branch=${QT_MODULE_BRANCH} \
    git://code.qt.io/qt/qtwebengine-chromium.git;name=chromium;branch=${QT_MODULE_BRANCH_CHROMIUM};destsuffix=git/src/3rdparty \
    file://0001-functions.prf-Make-sure-we-only-use-the-file-name-to.patch \
    file://0002-functions.prf-allow-build-for-linux-oe-g-platform.patch \
    file://0001-chromium-base.gypi-include-atomicops_internals_x86_g.patch \
    file://0001-Load-libEGL-and-libGLESv2-at-the-right-version.patch \
    file://0002-Support-wayland-egl-platform.patch \
    file://0003-Fix-problems-with-fpermissive-compiler-flag.patch \
"

SRCREV_qtwebengine = "e4361807da9db0609697e7a650947dbf26321cdc"
SRCREV_chromium = "0decf91700ad48612b885a549e79bb6252196d4d"
