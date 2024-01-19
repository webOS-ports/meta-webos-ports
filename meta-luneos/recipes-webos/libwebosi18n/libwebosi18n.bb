
SUMMARY = "libwebosi18n library can be used by non-QT C++ components for localization"
# Copyright (c) 2013-2023 LG Electronics, Inc.
AUTHOR = "Seonmi Jin <seonmi1.jin@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=70ae4f5a391a0b9df4abb7d3eb86aa27 \
"

DEPENDS = "libpbnjson boost"

WEBOS_VERSION = "1.0.1-8_af063dc035e950fda7967144c0e4fa8acc7ded91"
PR = "r3"

inherit webos_cmake
inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

