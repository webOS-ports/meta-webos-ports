# Copyright (c) 2020-2025 LG Electronics, Inc.

SUMMARY = "Memory Manager"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 glib-2.0-native luna-service2 libpbnjson pmloglib python3-packaging-native"

WEBOS_VERSION = "1.0.0-65_701456ce1ba2d761b244dd106f496a5e059ee6d6"
PR = "r14"

inherit webos_component
inherit webos_cmake
inherit webos_enhanced_submissions
inherit webos_daemon
inherit webos_system_bus
inherit webos_public_repo
inherit python3native

SRC_URI = " \
    ${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-com.webos.service.memorymanager-Fix-outbound-permiss.patch \
"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "memorymanager.service.in"

# ERROR: com.webos.service.memorymanager-1.0.0-63-r12 do_package_qa: QA Issue: File /usr/src/debug/com.webos.service.memorymanager/1.0.0-63/src/memorymanager/MMBus.c in package com.webos.service.memorymanager-src contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
