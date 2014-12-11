# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Library for dynamically generating webOS system bus role files for webOS JavaScript services"
SECTION = "webos/libs"
AUTHOR = "Seokhyon Seong <seokhyon.seong@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "2.1.0-20_cef54033f9a0e6a2c0dbf0c8cf3f9fcbb39979d9"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions 
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/rolegen"

inherit webos-ports-submissions
SRCREV = "b293a3f8b05f83b36b1b673c4e7dd1c18bfcf768"
