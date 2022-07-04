# Copyright (c) 2014-2021 LG Electronics, Inc.

SUMMARY = "QML widgets and runtime framework for webOS apps"
AUTHOR = "Mikko Levonmaa <mikko.levonmaa@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=9e100013a76448cbe7c14134b0974453 \
"

DEPENDS = "qt-features-webos qtdeclarative qtwayland-webos pmloglib luna-service2 qttools-native"
DEPENDS:append = " ${@ 'qtshadertools-native' if d.getVar('QT_VERSION', True) == '6' else '' }"
RDEPENDS:${PN} = "qtgraphicaleffects-qmlplugins"

RPROVIDES:${PN}-examples = " \
    eos.bare \
    eos.widgetgallery \
"

#WEBOS_VERSION = "1.0.0-161_a6969a4cb6e95949af68e316c8414951b0c8fc9b"
#PR = "r35"

inherit webos_qmake6
inherit pkgconfig
#inherit webos_enhanced_submissions
#inherit webos_machine_dep
inherit webos_app_generate_security_files
inherit webos_filesystem_paths
#inherit webos_distro_variant_dep
#inherit webos_qmllint
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
           file://0001-AppLoader-add-import-path-for-QML-apps.patch \
           file://0002-Setup-missing-env-variables.patch \
           file://0003-qml-runner-don-t-set-default-value-for-WEBOS_WINDOW_.patch \
           "

S = "${WORKDIR}/git"

SRCREV = "8f4f4b3942755aa4f57de80586aac7aa47b59ddc"

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
