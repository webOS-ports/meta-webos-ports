# Copyright (c) 2013-2024 LG Electronics, Inc.

SUMMARY = "Bootd single-shot launching service"
DESCRIPTION = "Bootd is a simplified upstart-like component. It provides automatic single-shot launching at boot time"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "systemd luna-service2 libpbnjson pmloglib glib-2.0 boost gtest"

WEBOS_VERSION = "2.0.0-21_7e47e2b3527b3ec9680f5ee61c985885f8688dc3"
PR = "r17"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "bootd.service"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-DefaultBootSequencer.cpp-Add-LuneOS-bits.patch \
"
S = "${WORKDIR}/git"

FILES:${PN} += "${webos_sysbus_datadir}"
