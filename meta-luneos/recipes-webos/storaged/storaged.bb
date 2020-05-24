# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Mass Storage Mode Manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c glib-2.0"
RDEPENDS_${PN} = "bash"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

PV = "2.1.0-5+git${SRCPV}"
SRCREV = "82199f26cc6786e8a7e6012293f8285ba21ae0e3"
S = "${WORKDIR}/git"
