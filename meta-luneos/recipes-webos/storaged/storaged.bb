# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Mass Storage Mode Manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c glib-2.0"
RDEPENDS:${PN} = "bash"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd
inherit pkgconfig

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

PV = "2.1.0-5+git${SRCPV}"
SRCREV = "bfc86d19cd51886ca76da9f3a8388f3570360c65"
S = "${WORKDIR}/git"
