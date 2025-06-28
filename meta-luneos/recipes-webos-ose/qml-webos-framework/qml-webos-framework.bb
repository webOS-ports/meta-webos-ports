# Copyright (c) 2014-2024 LG Electronics, Inc.

SUMMARY = "QML widgets and runtime framework for webOS apps"
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=9e100013a76448cbe7c14134b0974453 \
"

DEPENDS = "qt-features-webos qtdeclarative qtwayland-webos pmloglib luna-service2 qttools-native"
DEPENDS:append = " ${@ 'qtshadertools-native' if d.getVar('QT_VERSION')[0] == '6' else '' }"
RDEPENDS:${PN} = "qt5compat-qmlplugins"

RPROVIDES:${PN}-examples = " \
    eos.bare \
    eos.widgetgallery \
"

WEBOS_VERSION = "1.0.0-169_5a2860ca78815cb135dc4120f204c31bad5d7ab9"
PR = "r38"

inherit webos_qmake6
inherit pkgconfig
inherit webos_app_generate_security_files
inherit webos_filesystem_paths
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-AppLoader-add-import-path-for-QML-apps.patch \
    file://0002-Setup-missing-env-variables.patch \
    file://0003-qml-runner-don-t-set-default-value-for-WEBOS_WINDOW_.patch \
    file://0004-RoundedRectangle.qml-Use-GraphicalEffects-from-Qt5Co.patch \
    file://0005-AppLoader-use-old-syntax-for-quit-connect.patch \
    file://0006-com.webos.app.qmlrunner.role.json.in-Add-trustLevel.patch \
    file://0007-WebOSQuickWindow-make-setWindowPropery-Q_INVOKABLE.patch \
    file://0008-runner-debug-use-WEBOS_INSTALL_BINS-as-other-binarie.patch \
    file://0009-WebOSQuickWindow-use-APP_ID-env-variable-for-appId.patch \
"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

# Perform extra QML validation
#WEBOS_QMLLINT_EXTRA_VALIDATION = "1"

FILES:${PN} += "${OE_QMAKE_PATH_QML}/Eos/*"

PACKAGES += "${PN}-examples"
FILES:${PN}-examples += " \
    ${webos_applicationsdir}/* \
    ${datadir}/qml/locales/${BPN}/ \
"

# unit-tests
PACKAGES =+ "${PN}-tests"
FILES:${PN}-tests += "${datadir}/booster/tests/*"

# SDK tools
PACKAGES += "${PN}-tools"
FILES:${PN}-tools += "${webos_sdkdir}/*"

# we don't provide cmake tests
EXTRA_QMAKEVARS_POST += "CONFIG-=create_cmake"

PR:append = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'smack1', '', d)}"
PATCH_SMACK = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'file://0001-Add-SMACK-security-labeling.patch', '', d)}"
SRC_URI:append = " ${PATCH_SMACK}"

# ERROR: qml-webos-framework-1.0.0-169-r38 do_package_qa: QA Issue: File /usr/src/debug/qml-webos-framework/1.0.0-169/src/Eos/Items/qrc_beziergon.cpp in package qml-webos-framework-src contains reference to TMPDIR
# File /usr/src/debug/qml-webos-framework/1.0.0-169/examples/com.webos.exampleapp.canvastablet/.rcc/qrc_com.webos.exampleapp.canvastablet.cpp in package qml-webos-framework-src contains reference to TMPDIR
# File /usr/src/debug/qml-webos-framework/1.0.0-169/examples/com.webos.exampleapp.tabletevent/.rcc/qrc_com.webos.exampleapp.tabletevent.cpp in package qml-webos-framework-src contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
