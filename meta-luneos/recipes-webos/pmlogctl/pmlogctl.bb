# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "webOS logging control application"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib"

PV = "3.0.0-1+git${SRCPV}"
SRCREV = "ca750ec9f18a46faff18657d730de6fb7b4de31c"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
