# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Palm's Better Native JSON library"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "yajl glib-2.0 gperf-native lemon-native gmp uriparser"

WEBOS_VERSION = "2.4.0-29_da8f050597c3bd012d99ad6f454824c0317790f4"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_library

# These are the defaults, but explicitly specify so that readers know they exist
EXTRA_OECMAKE += "-DWITH_DOCS:BOOL=FALSE -DWITH_TESTS:BOOL=FALSE"

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
