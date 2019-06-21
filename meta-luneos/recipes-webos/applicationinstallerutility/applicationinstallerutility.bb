# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY="The Application Installer Utility supports the installing and removing of applications on a webOS/LuneOS device."
AUTHOR = "Seokjun Lee <sseokjun.lee@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib openssl glib-2.0"

PV = "3.0.0-1+git${SRCPV}"
SRCREV = "d21cc8579e1079af820d542e14f7c1855cf15a4d"

inherit webos_cmake
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"