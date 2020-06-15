# Copyright (c) 2013-2019 LG Electronics, Inc.

SUMMARY = "Common Qt features for webOS components"
AUTHOR = "Anupam Kaul <anulam.kaul@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qtbase"

#WEBOS_VERSION = "1.0.0-42_977a5b1e830b4629436cc3e77c2cc7282d8fa4cf"
PR = "r4"

PV = "1.0.0-42"
SRCREV = "b2d6fdbd2562118de5211b706103be5faac40e07"

inherit webos_qmake5
#inherit webos_enhanced_submissions
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN}-dev += "${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs"

# An empty package is needed to satisfy package dependencies when building bdk.
ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native"

do_configure_class-native() {
    ${OE_QMAKE_QMAKE} ${OE_QMAKE_DEBUG_OUTPUT} -r ${S}/tools/generate_qmap
}

do_install_class-native() {
    oe_runmake install INSTALL_ROOT=${D}
}
