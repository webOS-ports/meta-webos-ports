# Copyright (c) 2017-2023 LG Electronics, Inc.

SUMMARY = "webOS connman adapter support library"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=1c44bb8384dc6f3479834afcdfa98054 \
"

WEBOS_VERSION = "1.0.0-4_a59127baf2ddebc99874f714c9fe5528772e94d1"
PR = "r2"

PV = "1.0.0-4+git"
SRCREV = "a59127baf2ddebc99874f714c9fe5528772e94d1"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib luna-prefs wca-support-api"

RDEPENDS:${PN} = "iw"

inherit webos_cmake
inherit pkgconfig
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Revert-Removing-support-for-com.webos.service.wan-se.patch \
"
S = "${WORKDIR}/git"
