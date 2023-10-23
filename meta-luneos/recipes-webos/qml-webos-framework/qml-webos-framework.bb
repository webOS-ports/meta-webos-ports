# Copyright (c) 2014-2023 LG Electronics, Inc.

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

WEBOS_VERSION = "1.0.0-166_976ef6a9f06993cbaf2ed3babc6dcb96dd3b1b1f"
PR = "r35"

PV = "1.0.0-166+git"
SRCREV = "976ef6a9f06993cbaf2ed3babc6dcb96dd3b1b1f"

inherit webos_qmake6
inherit pkgconfig
inherit webos_app_generate_security_files
inherit webos_filesystem_paths
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-AppLoader-add-import-path-for-QML-apps.patch \
    file://0002-Setup-missing-env-variables.patch \
    file://0003-qml-runner-don-t-set-default-value-for-WEBOS_WINDOW_.patch \
    file://0004-RoundedRectangle.qml-Use-GraphicalEffects-from-Qt5Co.patch \
    file://0005-AppLoader-use-old-syntax-for-quit-connect.patch \
    file://0006-com.webos.app.qmlrunner.role.json.in-Add-trustLevel.patch \
    file://0007-WebOSQuickWindow-make-setWindowPropery-Q_INVOKABLE.patch \
    file://0008-runner-debug-use-WEBOS_INSTALL_BINS-as-other-binarie.patch \
"

S = "${WORKDIR}/git"

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
