# Copyright (c) 2019-2022 LG Electronics, Inc.

SUMMARY = "Surface Manager for webOS Auto Reference UX"
AUTHOR  = "Jaeyoon Jung <jaeyoon.jung@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=1bbf047e5c70344c074aaaa58a668952 \
"

DEPENDS = "luna-surfacemanager qt-features-webos"
RDEPENDS:${PN} = " \
    luna-surfacemanager-base \
    qtbase-plugins-webos \
"

SRCREV = "52ae0dcd3b1923bce9287bb1923fcd45871b318c"
PV = "0.0.1-39+git${SRCPV}"
PR = "r7"

inherit webos_qmake6
inherit webos_public_repo
# many failures like:
# usr/lib/qml/WebOSCompositor/AutoRSE/WebOSCompositor/views/WebOSAutoSystemUISurface.qml:21:1: SystemUISurface was not found. Did you add all import paths?
# SystemUISurface {
# ^^^^^^^^^^^^^^^
# inherit webos_qmllint
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

# Perform extra QML validation
WEBOS_QMLLINT_EXTRA_VALIDATION = "1"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

FILES:${PN} += " \
    ${OE_QMAKE_PATH_QML}/WebOSCompositor \
    ${OE_QMAKE_PATH_PLUGINS}/compositor \
"

# Doesn't respect libdir variable and installs in ${base_libdir}/plugins instead of OE_QMAKE_PATH_PLUGINS (${libdir}/plugins)
# which doesn't case issue in OSE due to usrmerge
FILES:${PN} += " \
    ${base_libdir}/plugins/compositor \
"
