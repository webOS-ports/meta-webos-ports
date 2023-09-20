# Copyright (c) 2013-2022 LG Electronics, Inc.

SUMMARY = "Shareable QML components for webOS"
AUTHOR = "Anupam Kaul <anupam.kaul@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=9dc14fd8aee1928cb4947063ac388f74 \
"

DEPENDS = "qtdeclarative pmloglib qt-features-webos luna-service2 glib-2.0"

SRCREV = "f49bdc70c97af4fa33f99cf7885f631051141a3a"

inherit webos_qmake6
inherit pkgconfig
inherit webos_lttng
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

# Enable LTTng tracing capability when enabled in webos_lttng class
EXTRA_QMAKEVARS_PRE += "${@ 'CONFIG+=lttng' if '${WEBOS_LTTNG_ENABLED}' == '1' else '' }"

# Base directory of localization data
EXTRA_QMAKEVARS_PRE += "WEBOS_QT_LOCALIZATION_QM_BASEDIR=${datadir}/qml/locales"

FILES:${PN} += "${OE_QMAKE_PATH_QML}"
