# Copyright (c) 2013-2024 LG Electronics, Inc.

SUMMARY = "Maliit Input Method Framework"
DESCRIPTION = "This is the webOS edition of the Maliit input method framework. It differs from upstream in that it supports a hardware keyboard using the wayland protocol."
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/base"
LICENSE = "LGPL-2.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPL;md5=5c917f6ce94ceb8d8d5e16e2fca5b9ad \
    file://oss-pkg-info.yaml;md5=1b633545a82af651ad37b9f03288651e \
"

DEPENDS = "qtbase qtdeclarative qtwayland-webos libxkbcommon pmloglib luna-service2 glib-2.0 udev wayland qt-features-webos"
# imemanager was pulled in here purely for its LS2 files - the
# com.webos.service.ime role/service/permission set that MaliitServer registers
# under. Those describe this package's own binary, so they now live in the
# maliit-framework-webos source tree under service/ and imemanager is gone,
# taking libplugin-global.so, its 8.6 MB of .dic files and the openwnn-webos
# libWnnJpn.so it dragged in with it. The LuneOS keyboard
# (libluneos-keyboard-plugin.so, MALIIT_DEFAULT_PLUGIN below) was always the
# active plugin; the global one was only ever loaded and then ignored.
RDEPENDS:${PN} = "qtbase-plugins configd"

# The com.webos.service.ime.* files this package now installs are at paths
# imemanager owned until now, so an upgrade over an installed imemanager is a
# file conflict unless this package is declared to supersede it. RPROVIDES
# additionally keeps anything that still RDEPENDS on the old name resolvable.
RPROVIDES:${PN} += "imemanager"
RREPLACES:${PN} += "imemanager"
RCONFLICTS:${PN} += "imemanager"

PACKAGECONFIG[libim] = "CONFIG+=enable-libim,CONFIG-=enable-libim,libim"

# Built from the webOS-ports fork rather than webosose plus a patch stack: the
# service/ LS2 files, the audit fixes, and what used to be the 0001-0003 patch
# series in this directory all live there as commits, so nothing is applied on
# top any more. Pinned with a plain SRCREV - submission tags are a webosose
# convention and this branch carries none past submissions/103.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"

SRCREV = "d31f37a17887dc4194326e281bbff2d137934e09"

# Set outright rather than derived from a submission tag, and deliberately not
# "0.99.0+20-104". webos_enhanced_submissions appended SRCPV here, so the
# shipped r37 package was 0.99.0+20-1030+71e5f78c3c; opkg compares the digit
# run after "0.99.0+20-", and 104 sorts below 1030, so the obvious next
# submission number would read as a downgrade on update. "-1040" keeps the
# submission-104 reading and still sorts above. Without the submissions class
# PKGV is now just PV, so what is written here is what ships.
PV = "0.99.0+20-1040"

# Bump PR on every SRCREV move from here on. The submissions class used to
# append SRCPV to PKGV, so a new revision changed the package version by
# itself; now PKGV is just PV, and a SRCREV bump alone rebuilds but produces an
# identically-versioned package that opkg sees no reason to install.
PR = "r40"

inherit pkgconfig
inherit webos_qmake6
inherit webos_filesystem_paths
inherit webos_ports_ose_repo
inherit features_check
ANY_OF_DISTRO_FEATURES = "vulkan opengl"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

EXTRA_QMAKEVARS_PRE += "CONFIG+=wayland MALIIT_DEFAULT_PLUGIN=libluneos-keyboard-plugin.so MALIIT_DEFAULT_HW_PLUGIN=libluneos-keyboard-plugin.so CONFIG+=noxcb CONFIG+=nodoc CONFIG+=notests CONFIG+=noexamples"
EXTRA_QMAKEVARS_PRE += "INCDIR=${STAGING_INCDIR} INCLUDEDIR=${STAGING_INCDIR} LIBDIR=${STAGING_LIBDIR} MALIIT_PLUGINS_DIR=${libdir}/maliit/plugins MALIIT_DATA_DIR=${webos_execstatedir}/maliit"
EXTRA_QMAKEVARS_PRE += "MALIIT_VERSION=${PV}"
EXTRA_QMAKEVARS_PRE += "WEBOS_TARGET_MACHINE_IMPL=${WEBOS_TARGET_MACHINE_IMPL}"
EXTRA_QMAKEVARS_PRE += "${EXTRA_CONF_PACKAGECONFIG}"

# .pc generation should be fixed to use correct paths
SSTATE_SCAN_FILES += "*.prf *.pc"

SRC_URI += " \
    file://maliit-server.conf \
    file://maliit-server.service \
    file://maliit-server@.service \
    file://maliit-server.sh.in \
    file://maliit-env.conf \
"

inherit systemd

# service/ in the source tree installs the com.webos.service.ime LS2 files via
# webos-service.prf. This class adds ${webos_sysbus_*} to FILES:${PN} and
# generates maliit-framework-webos.manifest.json from them at package time.
# Its own do_install would additionally copy ${S}/service/*.json into the
# deprecated roles-prv/roles-pub locations, so skip its tasks - same reason
# imemanager set this.
inherit webos_system_bus
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "maliit-server.service"

do_install:append() {
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${UNPACKDIR}/maliit-server.conf ${D}${sysconfdir}/dbus-1/system.d/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/maliit-server.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${UNPACKDIR}/maliit-server@.service ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/system/scripts
    install -m 0755 ${UNPACKDIR}/maliit-server.sh.in ${D}${systemd_unitdir}/system/scripts/maliit-server.sh

    install -d ${D}${sysconfdir}/maliit
    install -m 0644 ${UNPACKDIR}/maliit-env.conf ${D}${sysconfdir}/maliit/

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

# ERROR: maliit-framework-webos-0.99.0+20-102-r36 do_package_qa: QA Issue: File /usr/lib/mkspecs/features/maliit-framework.prf in package maliit-framework-webos-dev contains reference to TMPDIR
# File /usr/lib/mkspecs/features/maliit-defines.prf in package maliit-framework-webos-dev contains reference to TMPDIR
# File /usr/lib/mkspecs/features/maliit-plugins.prf in package maliit-framework-webos-dev contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
