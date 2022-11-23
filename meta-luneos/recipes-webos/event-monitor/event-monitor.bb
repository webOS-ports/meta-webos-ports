# Copyright (c) 2015-2018 LG Electronics, Inc.

SUMMARY = "Event Monitoring Service for generic notifications"
AUTHOR = "Viesturs Zarins <viesturs.zarins@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 pmloglib libpbnjson libwebosi18n"

PV = "1.1.0-1+git${SRCPV}"
SRCREV = "9f143e14050415e6104c96742a8f8685c6ed104c"

inherit webos_cmake
inherit webos_system_bus
inherit webos_event_monitor_plugin
inherit webos_ports_ose_repo
inherit webos_systemd
inherit pkgconfig

WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

#Enable/disable mock plugin
EXTRA_OECMAKE += "-DBUILD_MOCK_PLUGIN:BOOL='NO'"

#webos_event_monitor_plugin depends on event-monitor, remove circular dependency
WEBOS_EVENT_MONITOR_PLUGIN_DEPENDS = ""
