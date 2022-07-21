# Copyright (c) 2021-2022 LG Electronics, Inc.

SUMMARY = "webOS QML app components"
AUTHOR = "Jongson Kim <jongson.kim@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
  file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"

DEPENDS = "qtdeclarative luna-service2 glib-2.0"
DEPENDS:append = " ${@ 'qtshadertools-native' if d.getVar('QT_VERSION', True) == '6' else '' }"

PV = "1.0.0-6+git${SRCPV}"
SRCREV = "128516aa9fedb645b2989618b50ec9e1a8e1f6aa"

inherit webos_qmake6
inherit pkgconfig
#inherit webos_enhanced_submissions
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
           file://0001-FontStyle.qml-Use-Prelude-on-LuneOS-instead-of-Museo.patch \
"

S = "${WORKDIR}/git"

QMAKE_PROFILES = "${S}/qml-app-components.pro"
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

FILES:${PN} += "${OE_QMAKE_PATH_QML}/QmlAppComponents/*"
 
