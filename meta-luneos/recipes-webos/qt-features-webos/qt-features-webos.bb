# Copyright (c) 2013-2021 LG Electronics, Inc.

SUMMARY = "Common Qt features for webOS components"
AUTHOR = "Anupam Kaul <anulam.kaul@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=35b2f0d27bf4833f2764dfe176fa1e9c \
"

DEPENDS = "qtbase"

WEBOS_VERSION = "1.0.0-50_e3cd3c25717605a153d10fcccbf35893a38ebb8b"
PR = "r8"

SRCREV = "7ba4ebe6d95591a31c8e072da5fd68adcfcb368a"

inherit webos_qmake5
#inherit webos_enhanced_submissions
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
           file://0001-Do-not-depend-on-QtInputSupport-for-generate_qmap.patch \
           "
S = "${WORKDIR}/git"

FILES:${PN}-dev += "${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs"

# An empty package is needed to satisfy package dependencies when building bdk.
ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native"

do_configure:class-native() {
    ${OE_QMAKE_QMAKE} ${OE_QMAKE_DEBUG_OUTPUT} -r ${S}/tools/generate_qmap
}

do_install:class-native() {
    oe_runmake install INSTALL_ROOT=${D}
}
