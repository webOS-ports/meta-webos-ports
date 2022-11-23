# Copyright (c) 2020-2021 LG Electronics, Inc.

# Confidential computer software. Valid license from LG required for
# possession, use or copying. Consistent with FAR 12.211 and 12.212,
# Commercial Computer Software, Computer Software Documentation, and
# Technical Data for Commercial Items are licensed to the U.S. Government
# under vendor's standard commercial license.

SUMMARY = "Mediagallery QML App"
AUTHOR = "Hyein Lee<hyein1.lee@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
"

DEPENDS = "qtdeclarative pmloglib luna-service2 libpbnjson"
DEPENDS:append = " ${@ 'qtshadertools-native' if d.getVar('QT_VERSION', True) == '6' else '' }"

RDEPENDS:${PN} += "qml-webos-framework qml-webos-bridge qml-app-components"

inherit webos_public_repo
inherit webos_localizable
inherit webos_qmake6
inherit systemd
inherit webos_system_bus
inherit webos_app


PV = "1.0.0-15+git${SRCPV}"
SRCREV = "30b7565c59324dd0ce153e57ee7b4ab11b1aea38"

#WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

QMAKE_PROFILES = "${S}/com.webos.app.mediagallery.pro"
QE_QMAKE_PATH_HEADERS = "${QE_QMAKE_PATH_QT_HEADERS}"

FILES:${PN} += "${webos_applicationsdir}"
