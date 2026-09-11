# Copyright (c) 2012-2013 LG Electronics, Inc.
# Copyright (c) 2023 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "Battery & charger daemon"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c glib-2.0"

PV = "1.0.0-1+git"
PR = "r1"
SRCREV = "622c176e8d55de4b266843be4f2185e876f3d87d"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
LUNEOS_SYSTEMD_SERVICE = "${PN}.service"
