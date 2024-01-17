# Copyright (c) 2013-2023 LG Electronics, Inc.

SUMMARY = "Maliit Input Method Framework"
DESCRIPTION = "This is the webOS edition of the Maliit input method framework. It differs from upstream in that it supports a hardware keyboard using the wayland protocol."
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/base"
LICENSE = "LGPL-2.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPL;md5=5c917f6ce94ceb8d8d5e16e2fca5b9ad \
    file://oss-pkg-info.yaml;md5=1b633545a82af651ad37b9f03288651e \
"

DEPENDS = "qtbase qtdeclarative qtwayland-webos libxkbcommon pmloglib luna-service2 glib-2.0 udev wayland"
RDEPENDS:${PN} = "qtbase-plugins configd imemanager"

PACKAGECONFIG[libim] = "CONFIG+=enable-libim,CONFIG-=enable-libim,libim"

WEBOS_VERSION = "0.99.0+20-102_b3c5fe41a33b6dd3d5c11b704c6ff2c8974ef7b6"
PR = "r35"

PV = "0.99.0+20-102+git${SRCPV}"
SRCREV = "b3c5fe41a33b6dd3d5c11b704c6ff2c8974ef7b6"

inherit pkgconfig
inherit webos_qmake6
inherit webos_filesystem_paths
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

EXTRA_QMAKEVARS_PRE += "CONFIG+=wayland MALIIT_DEFAULT_PLUGIN=libluneos-keyboard-plugin.so CONFIG+=noxcb CONFIG+=nodoc CONFIG+=notests CONFIG+=noexamples"
EXTRA_QMAKEVARS_PRE += "INCDIR=${STAGING_INCDIR} INCLUDEDIR=${STAGING_INCDIR} LIBDIR=${STAGING_LIBDIR} MALIIT_PLUGINS_DIR=${libdir}/maliit/plugins MALIIT_DATA_DIR=${webos_execstatedir}/maliit"
EXTRA_QMAKEVARS_PRE += "MALIIT_VERSION=${PV}"
EXTRA_QMAKEVARS_PRE += "WEBOS_TARGET_MACHINE_IMPL=${WEBOS_TARGET_MACHINE_IMPL}"
EXTRA_QMAKEVARS_PRE += "${EXTRA_CONF_PACKAGECONFIG}"

# .pc generation should be fixed to use correct paths
SSTATE_SCAN_FILES += "*.prf *.pc"

SRC_URI += " \
    file://0001-Correctly-detect-wayland-platform.patch \
    file://maliit-server.conf \
    file://maliit-server.service \
    file://maliit-server@.service \
    file://maliit-server.sh.in \
    file://maliit-env.conf \
"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "maliit-server.service"

do_install:append() {
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${WORKDIR}/maliit-server.conf ${D}${sysconfdir}/dbus-1/system.d/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/maliit-server.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/maliit-server@.service ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/system/scripts
    install -m 0755 ${WORKDIR}/maliit-server.sh.in ${D}${systemd_unitdir}/system/scripts/maliit-server.sh

    install -d ${D}${sysconfdir}/maliit
    install -m 0644 ${WORKDIR}/maliit-env.conf ${D}${sysconfdir}/maliit/

    install -d ${D}${localstatedir}/lib/maliit
}

do_install:append() {
    # headers
    install -d ${D}${includedir}/maliit
    install -v -m 644 ${S}/common/maliit/*.h ${D}${includedir}/maliit/
    install -d ${D}${includedir}/maliit/plugins
    install -v -m 644 ${S}/src/maliit/plugins/*.h ${D}${includedir}/maliit/plugins/
}

FILES:${PN}-dev += "${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs"

do_install:append() {
    sed -i 's@libdir=${STAGING_LIBDIR}@libdir=${libdir}@g' ${D}${libdir}/pkgconfig/*.pc
    sed -i 's@includedir=${STAGING_INCDIR}@includedir=${includedir}@g' ${D}${libdir}/pkgconfig/*.pc
}

FILES:${PN} += "${OE_QMAKE_PATH_QT_ARCHDATA} ${systemd_unitdir}/system/scripts"
