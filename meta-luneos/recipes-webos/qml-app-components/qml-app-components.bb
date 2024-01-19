# Copyright (c) 2021-2023 LG Electronics, Inc.

SUMMARY = "webOS QML app components"
AUTHOR = "KIEN TRUNG PHAM <kien2.pham@lge.com>"
SECTION = "webos/libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"

DEPENDS = "qtdeclarative luna-service2 glib-2.0"
DEPENDS:append = " ${@ 'qtshadertools-native' if d.getVar('QT_VERSION')[0] == '6' else '' }"

WEBOS_VERSION = "1.0.0-6_ad9b0aee66408b214d5f4d61b9912ed411da2f00"
PR = "r4"

inherit webos_qmake6
inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-FontStyle.qml-Use-Prelude-on-LuneOS-instead-of-Museo.patch \
"

S = "${WORKDIR}/git"

QMAKE_PROFILES = "${S}/qml-app-components.pro"
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

FILES:${PN} += "${OE_QMAKE_PATH_QML}/QmlAppComponents/*"
 
