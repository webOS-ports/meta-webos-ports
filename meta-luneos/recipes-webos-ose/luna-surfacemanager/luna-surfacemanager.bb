# Copyright (c) 2013-2024 LG Electronics, Inc.

SUMMARY = "The core of the Luna Surface Manager (compositor)"
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2c7c706c6a586a6abec428c64006d86b \
"

DEPENDS = "qtdeclarative wayland-native qtwayland qtwayland-native qt-features-webos pmloglib webos-wayland-extensions glib-2.0 qtwayland-webos"

WEBOS_VERSION = "2.0.0-395_4e88486015c69db462654a7ef797f5c6a56e616f"
PR = "r60"

inherit webos_qmake6
inherit pkgconfig
inherit webos_lttng
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Add-capability-to-pass-extra-options-to-surface-mana.patch \
    file://0002-WebOSShellSurface-add-setClientSize.patch \
    file://0003-webosscreenshot-respect-QT_OPENGL_ES.patch \
    file://0004-DefaultSettings.qml-Use-Prelude-for-LuneOS.patch \
    file://0005-Update-com.webos.surfacemanager.role.json.in.patch \
    file://0006-product.env.in-Make-it-work-with-non-drm-devices.patch \
    file://0007-Add-additional-permissions-for-org.webosports.notifi.patch \
    file://0008-base.pro-Remove-building-of-tests.patch \
    file://0009-com.webos.surfacemanager.perm.json-Add-permissions-f.patch \
    file://0010-qmldir-expose-NotificationService-component.patch \
    file://0011-Input-panel-tie-inputPanelRect-to-the-window-mask.patch \
    file://0012-WebOSSurfaceItem-close-Wayland-client-fallback-on-Cl.patch \
    file://0013-Wait-for-DRI-card-on-EGLFS-platform.patch \
"

S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "lsm-ready.path lsm-ready.service lsm-ready.target surface-manager.service surface-manager-daemon.service"

#SYSTEMD_INSTALL_PATH = "${systemd_system_unitdir}"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

# Enable LTTng tracing capability when enabled in webos_lttng class
EXTRA_QMAKEVARS_PRE += "${@ 'CONFIG+=lttng' if '${WEBOS_LTTNG_ENABLED}' == '1' else '' }"

EXTRA_QMAKEVARS_PRE += "${PACKAGECONFIG_CONFARGS}"

# We don't support configuring via cmake
EXTRA_QMAKEVARS_POST += "CONFIG-=create_cmake"

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
#    install -v -m 644 ${WORKDIR}/lsm-ready.path ${D}${SYSTEMD_INSTALL_PATH}/lsm-ready.path
#    install -v -m 644 ${WORKDIR}/lsm-ready.target ${D}${SYSTEMD_INSTALL_PATH}/lsm-ready.target
#    install -v -m 644 ${WORKDIR}/lsm-ready.service ${D}${SYSTEMD_INSTALL_PATH}/lsm-ready.service
#    install -v -m 644 ${WORKDIR}/surface-manager.service ${D}${SYSTEMD_INSTALL_PATH}/surface-manager.service
#    install -v -m 644 ${WORKDIR}/surface-manager-daemon.service ${D}${SYSTEMD_INSTALL_PATH}/surface-manager-daemon.service
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

PACKAGECONFIG = "compositor cursor-theme"

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
