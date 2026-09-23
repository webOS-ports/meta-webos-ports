# Copyright (c) 2016-2024 LG Electronics, Inc.

SUMMARY = "webOS extension for Qtwayland"
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=7187b1fb0318bb1af23edbf4237ee8b8 \
"

# Qt 6.10 moved the QtWayland client and qtwaylandscanner into qtbase, and
# meta-qt6 dropped the qtwayland native/nativesdk builds with it
# ("Adapt to QtWayland client move to QtBase"). qtwaylandscanner now comes from
# qtbase-native, gated on the wayland DISTRO_FEATURE which LuneOS sets.
DEPENDS = "qtwayland webos-wayland-extensions libxkbcommon qt-features-webos wayland-native qtbase-native wayland-protocols"

SRCREV = "032b6a86724ab844dfeefcd9a2fdb39def8ee1fe"

PV = "6.0.0-95"

PR = "r23"

#QT_BUILD_SYSTEM ?= "${@ 'cmake' if d.getVar('QT_VERSION')[0] == '6' else 'qmake' }"
#Force qmake for now since cmake gives build errors
# add_library cannot create ALIAS target "Qt::WebOSEglClientBuffer" because
#|   another target with the same name already exists.

QT_BUILD_SYSTEM = "qmake"

PACKAGECONFIG ??= ""

# qtwayland-webos_cmake.inc or qtwayland-webos_qmake.inc
require ${BPN}_${QT_BUILD_SYSTEM}.inc

inherit pkgconfig
inherit webos_lttng
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

RDEPENDS:${PN} += "qtbase-plugins"

# No debian package renaming
DEBIAN_NOAUTONAME:${PN} = "1"
DEBIAN_NOAUTONAME:${PN}-dbg = "1"
DEBIAN_NOAUTONAME:${PN}-dev = "1"

# Enable LTTng tracing capability when enabled in webos_lttng class
EXTRA_QMAKEVARS_PRE += "${@oe.utils.conditional('WEBOS_LTTNG_ENABLED', '1', 'CONFIG+=lttng', '', d)}"

FILES:${PN} += " \
    ${libdir}/plugins/*/*${SOLIBSDEV} \
"

FILES:${PN}-dev += " \
    ${libdir}/*.prl \
    ${libdir}/mkspecs/* \
"

# FIXME: weboscompositorextensionclient is deprecated and merged into
# webos-platform-interface. We provide weboscompositorextensionclient.pc
# as an alias of webos-platform-interface.pc so that components used to
# depend on weboscompositorextensionclient build and run as before.
# This should be removed once all these components have no dependency
# on weboscompositorextensionclient.
do_install:append() {
    ln -snvf webos-platform-interface.pc ${D}${libdir}/pkgconfig/weboscompositorextensionclient.pc
    sed -i 's@prefix=${STAGING_DIR_HOST}@prefix=@g ;s@-L${STAGING_DIR_HOST} @ @g;' ${D}${libdir}/pkgconfig/*.pc
    sed -i "s@-L${STAGING_LIBDIR}@-L\${libdir}@g" ${D}${libdir}/pkgconfig/*.pc
}

# ERROR: qtwayland-webos-6.0.0-93-r20 do_package_qa: QA Issue: File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-presentation-time.cpp in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-presentation-time.h in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-surface-group.h in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-surface-group.cpp in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-tablet.cpp in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-foreign.cpp in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-foreign.h in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-input-manager.cpp in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-input-manager.h in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-tablet.h in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-extension.cpp in package qtwayland-webos-src contains reference to TMPDIR
# File /usr/src/debug/qtwayland-webos/6.0.0-93/src/webos-platform-interface/qwayland-webos-extension.h in package qtwayland-webos-src contains reference to TMPDIR [buildpaths]
# ERROR: qtwayland-webos-6.0.0-93-r20 do_package_qa: QA Issue: File /usr/lib/libWebOSEglClientBuffer.prl in package qtwayland-webos-dev contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
