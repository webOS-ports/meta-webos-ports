# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "Bootd single-shot launching service"
DESCRIPTION = "Bootd is a simplified upstart-like component. It provides automatic single-shot launching at boot time"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "systemd luna-service2 libpbnjson pmloglib glib-2.0 boost gtest"

PV = "0.1.5+git${SRCPV}"
SRCREV = "a5be95b4cfd27a9ab7eb65de1bbb2f453f2e52a4"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES:${PN} += "${webos_sysbus_datadir}"

# gtest option
PACKAGES =+ "${PN}-tests"
FILES:${PN}-tests = "${libexecdir}/tests/*"
