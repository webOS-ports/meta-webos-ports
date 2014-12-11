# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "libsandbox is a collection of APIs for separating running programs"
SECTION = "webos/devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "2.0.0-16+git${SRCPV}"
SRCREV = "3c461c5958adb6fa0671e68a77c75efa6deb7314"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_machine_impl_dep

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_NO_STATIC_LIBRARIES_WHITELIST = "libsandbox.a"

ALLOW_EMPTY_${PN} = "1"
