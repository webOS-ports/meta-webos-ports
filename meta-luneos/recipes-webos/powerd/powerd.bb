# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Power policy daemon"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c glib-2.0"

PV = "4.0.0-25+git${SRCPV}"
SRCREV = "02a3584eb547339818e2c7ed9c1a56c7c88cc9f7"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"