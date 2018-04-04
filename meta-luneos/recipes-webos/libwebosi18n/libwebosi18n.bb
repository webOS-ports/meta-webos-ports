# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "libwebosi18n library can be used by non-QT C++ components for localization"
AUTHOR = "Edwin Hoogerbeets <edwin.hoogerbeets@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libpbnjson boost"

inherit webos_cmake
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRCREV = "5dce4ba2db88d46d9f3dcb772af326bfabeaa25d"
