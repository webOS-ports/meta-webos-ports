# Copyright (c) 2017-2024 LG Electronics, Inc.

SUMMARY = "webOS connman adapter support API"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=6846cc431bc5281393713ebd4300401d \
"

DEPENDS = "libpbnjson luna-service2"

WEBOS_VERSION = "1.0.0-5_117f8854f0682263a999d50b1f146b7b6d0d288f"
PR = "r3"

inherit pkgconfig
inherit webos_cmake
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Revert-Removing-support-for-com.webos.service.wan-se.patch \
"

S = "${WORKDIR}/git"
