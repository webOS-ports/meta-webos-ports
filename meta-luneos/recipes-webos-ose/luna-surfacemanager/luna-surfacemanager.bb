# Copyright (c) 2013-2024 LG Electronics, Inc.

SUMMARY = "The core of the Luna Surface Manager (compositor)"
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2c7c706c6a586a6abec428c64006d86b \
"

# Qt 6.10 moved the QtWayland client and qtwaylandscanner into qtbase, and
# meta-qt6 dropped the qtwayland native/nativesdk builds with it
# ("Adapt to QtWayland client move to QtBase"). qtwaylandscanner now comes from
# qtbase-native, gated on the wayland DISTRO_FEATURE which LuneOS sets.
DEPENDS = "qtdeclarative wayland-native qtwayland qtbase-native qt-features-webos pmloglib webos-wayland-extensions glib-2.0 qtwayland-webos"

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: what used to be the
# 0001-0024 patch series in this directory now lives as commits there, so
# nothing is applied on top any more. Pinned with a plain SRCREV - submission
# tags are a webosose convention and this branch carries none. The branch
# itself comes from webos_ports_ose_repo below.
SRCREV = "1113a6e06af8ff50702532a5d590aee7dd378ad9"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 2.0.0-423, so anything lower
# would look like a downgrade to opkg on an update.
PV = "2.0.0-424"

PR = "r65"

inherit webos_qmake6
inherit pkgconfig
inherit webos_lttng
WEBOS_GIT_PARAM_BRANCH = "herrie/cleanup"
inherit webos_ports_ose_repo
inherit features_check
ANY_OF_DISTRO_FEATURES = "vulkan opengl"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "lsm-ready.path lsm-ready.service lsm-ready.target surface-manager.service surface-manager-daemon.service"

#SYSTEMD_INSTALL_PATH = "${systemd_system_unitdir}"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

# Enable LTTng tracing capability when enabled in webos_lttng class
EXTRA_QMAKEVARS_PRE += "${@ 'CONFIG+=lttng' if '${WEBOS_LTTNG_ENABLED}' == '1' else '' }"

EXTRA_QMAKEVARS_PRE += "${PACKAGECONFIG_CONFARGS}"

# We don't support configuring via cmake
EXTRA_QMAKEVARS_POST += "CONFIG-=create_cmake"

# qtbase's wayland-scanner.prf (the qmake path; the prf moved to qtbase with
# the QtWayland client in 6.10) declares the qtwaylandscanner outputs with
# variable_out = HEADERS but no target_predeps, so the generated
# qwayland-server-*.h only appear in the Makefile as dependencies of objects
# whose source qmake could dep-scan. The moc-generated objects are not among
# them: .obj/moc_*.o depends only on its .moc/*.cpp, whose synthesized rule
# stops at the mocable header - yet the moc output includes the generated
# headers through the syncqt forwarding headers. On a fresh ${B} make -j
# therefore races moc-object compilation against qtwaylandscanner, and a hot
# ccache makes the compile side fast enough to lose reliably (moc_webossurfaceitem.o:
# fatal error: qwayland-server-webos-surface-group.h: No such file or directory).
# Run the scanner rules to completion before the parallel build so it starts
# with the headers on disk. qmake_all first: on a fresh ${B} the sub-Makefiles
# only come into existence as make descends the subdirs tree.
do_compile:prepend() {
    oe_runmake -C ${B} qmake_all
    oe_runmake -C ${B}/modules/weboscompositor \
        compiler_qtwayland_server_header_make_all \
        compiler_qtwayland_server_code_make_all
}

FILES:${PN}-dev += " \
    ${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs/* \
    ${OE_QMAKE_PATH_LIBS}/*.prl \
"

do_install:append() {
    sed -i 's@prefix=${STAGING_DIR_HOST}@prefix=@g;s@-L${STAGING_DIR_HOST} @ @g;' ${D}${libdir}/pkgconfig/*.pc
    sed -i "s@-L${STAGING_LIBDIR}@-L\${libdir}@g" ${D}${libdir}/pkgconfig/*.pc
    if ${@bb.utils.contains('PACKAGECONFIG', 'compositor', 'true', 'false', d)}; then
        install -d ${D}${datadir}/webos-keymap
        ${STAGING_DIR_NATIVE}${OE_QMAKE_PATH_QT_BINS}/generate_qmap ${D}${datadir}/webos-keymap/webos-keymap.qmap
    fi
	
    if ${@bb.utils.contains('IMAGE_FEATURES', 'webos-test', 'true', 'false', d)}; then
        mkdir -p ${D}${libdir}/${BPN}
        find ${B} -name \*.gcno -exec cp -t ${D}${libdir}/${BPN} {} \;
    fi
    
    # This dummy import conflicts with the ${OE_QMAKE_PATH_QML}/WebOSCompositor import we use for luna-next-cardshell
    rm -rf ${D}${OE_QMAKE_PATH_QML}/WebOSCompositorBase/imports/WebOSCompositor
#    install -v -m 644 ${UNPACKDIR}/lsm-ready.path ${D}${SYSTEMD_INSTALL_PATH}/lsm-ready.path
#    install -v -m 644 ${UNPACKDIR}/lsm-ready.target ${D}${SYSTEMD_INSTALL_PATH}/lsm-ready.target
#    install -v -m 644 ${UNPACKDIR}/lsm-ready.service ${D}${SYSTEMD_INSTALL_PATH}/lsm-ready.service
#    install -v -m 644 ${UNPACKDIR}/surface-manager.service ${D}${SYSTEMD_INSTALL_PATH}/surface-manager.service
#    install -v -m 644 ${UNPACKDIR}/surface-manager-daemon.service ${D}${SYSTEMD_INSTALL_PATH}/surface-manager-daemon.service
}

TARGET_CXXFLAGS:append = " ${@bb.utils.contains('IMAGE_FEATURES', 'webos-test', '--coverage -fprofile-dir=/tmp/luna-surfacemanager-gcov -O0', '', d)}"
TARGET_LDFLAGS:append = " ${@bb.utils.contains('IMAGE_FEATURES', 'webos-test', '--coverage', '', d)}"

VIRTUAL-RUNTIME_gpu-libs ?= ""
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_gpu-libs}"

# Select platform abstraction plugin
VIRTUAL-RUNTIME_lsm-qpa ?= ""
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_lsm-qpa}"

inherit webos_system_bus
#inherit webos_qmllint

# qt-features-webos have its own logic to install system bus files reason for
# that is because only qmake knows where substitued files will be placed.
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

# Perform extra QML validation
#WEBOS_QMLLINT_EXTRA_VALIDATION = "1"

PACKAGECONFIG ??= "multi-input"
PACKAGECONFIG[compositor] = "CONFIG+=compositor_base,,qt-features-webos-native"
PACKAGECONFIG[multi-input] = ",CONFIG+=no_multi_input,"
PACKAGECONFIG[cursor-theme] = "CONFIG+=cursor_theme,,"

# The upstream test suite under base/tests. Off by default: it ships an ACG
# test app and a second set of LS2 role and permission files, which a
# production image has no use for. The webos-test image feature turns it on
# for a test image, the same feature the coverage build above keys off.
PACKAGECONFIG[tests] = "CONFIG+=webos_tests,,"

PACKAGECONFIG = "compositor cursor-theme"
PACKAGECONFIG += "${@bb.utils.contains('IMAGE_FEATURES', 'webos-test', 'tests', '', d)}"

PACKAGE_BEFORE_PN = "${PN}-gcov"

FILES:${PN}-gcov = " \
    ${libdir}/${BPN}/*.gcno \
"

PACKAGES =+ "${PN}-base ${PN}-base-tests"

FILES:${PN}-base += " \
    ${OE_QMAKE_PATH_QML}/WebOSCompositorBase/ \
    ${OE_QMAKE_PATH_QML}/WebOSCompositor/ \
    ${OE_QMAKE_PATH_BINS}/ \
    ${datadir}/icons/ \
    ${datadir}/webos-keymap/webos-keymap.qmap \
    ${sysconfdir}/surface-manager.d/ \
    ${webos_sysbus_apipermissionsdir} \
    ${webos_sysbus_groupsdir} \
    ${webos_sysbus_servicedir} \
    ${webos_sysbus_manifestsdir}/luna-surfacemanager.manifest.json \
    ${webos_sysbus_permissionsdir}/com.webos.surfacemanager.perm.json \
    ${webos_sysbus_rolesdir}/com.webos.surfacemanager.role.json \
"

FILES:${PN}-base-tests += " \
    ${webos_applicationsdir}/ \
    ${webos_sysbus_manifestsdir}/ \
    ${webos_sysbus_permissionsdir}/ \
    ${webos_sysbus_rolesdir}/ \
    ${webos_testsdir}/${BPN}/ \
"

RDEPENDS:${PN}-base += "luna-surfacemanager-conf xkeyboard-config qml-webos-framework qml-webos-bridge qml-webos-components"
