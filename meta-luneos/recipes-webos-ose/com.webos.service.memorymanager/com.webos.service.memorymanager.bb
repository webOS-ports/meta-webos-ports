# Copyright (c) 2020-2024 LG Electronics, Inc.

SUMMARY = "Memory Manager"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 glib-2.0-native luna-service2 libpbnjson pmloglib python3-packaging-native"

WEBOS_VERSION = "1.0.0-63_db714511a2ba4b85c8b894b07f7880add4e527a8"
PR = "r12"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit pkgconfig
inherit python3native

SRC_URI = " \
    ${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-com.webos.service.memorymanager-Fix-outbound-permiss.patch \
"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "memorymanager.service.in"
