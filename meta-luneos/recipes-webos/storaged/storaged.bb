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

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

PV = "2.1.0-5+git${SRCPV}"
SRCREV = "060edf7ea53e6129a19a5716980d377049b6a82c"
S = "${WORKDIR}/git"
