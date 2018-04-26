# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Sleep scheduling policy daemon"
AUTHOR = "Sapna Todwal <sapna.todwal@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c libxml2 sqlite3 glib-2.0"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

PV = "2.0.0-1+git${SRCPV}"

SRC_URI = "git://github.com/herrie82/sleepd.git;branch=webosose"
S = "${WORKDIR}/git"

SRCREV = "abaf192e0444d0294e074901ad6281f849143b1b"
