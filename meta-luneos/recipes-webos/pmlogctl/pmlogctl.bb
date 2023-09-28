# Copyright (c) 2012-2023 LG Electronics, Inc.

SUMMARY = "webOS logging control application"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "pmloglib"

WEBOS_VERSION = "3.0.0-2_a6ac2b162550e69d2a2bfe5b77eaff24e1a92184"
PR = "r4"

PV = "3.0.0-2+git${SRCPV}"
SRCREV = "a6ac2b162550e69d2a2bfe5b77eaff24e1a92184"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
