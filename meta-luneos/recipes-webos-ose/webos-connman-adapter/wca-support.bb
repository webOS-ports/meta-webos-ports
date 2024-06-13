# Copyright (c) 2017-2024 LG Electronics, Inc.

SUMMARY = "webOS connman adapter support library"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=1c44bb8384dc6f3479834afcdfa98054 \
"

WEBOS_VERSION = "1.0.0-5_8bb192e7fb628c4dc5172a00f2ec518969f47fe7"
PR = "r3"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib luna-prefs wca-support-api"

RDEPENDS:${PN} = "iw"

inherit webos_cmake
inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Revert-Removing-support-for-com.webos.service.wan-se.patch \
"
S = "${WORKDIR}/git"
