# Copyright (c) 2015-2023 LG Electronics, Inc.

SUMMARY = "Event Monitoring Service for generic notifications"
AUTHOR = "Viesturs Zarins <viesturs.zarins@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 pmloglib libpbnjson libwebosi18n"

WEBOS_VERSION = "1.1.0-14_ebed44ed82acfbb2c6e4193d21eb22742c6d5ad2"

PV = "1.1.0-14+git"
SRCREV = "ebed44ed82acfbb2c6e4193d21eb22742c6d5ad2"

inherit webos_cmake
inherit webos_system_bus
inherit webos_event_monitor_plugin
inherit webos_public_repo
inherit webos_systemd
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Add-trustLevel.patch \
"

S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "event-monitor.service"

#Enable/disable mock plugin
EXTRA_OECMAKE += "-DBUILD_MOCK_PLUGIN:BOOL='NO'"

#webos_event_monitor_plugin depends on event-monitor, remove circular dependency
WEBOS_EVENT_MONITOR_PLUGIN_DEPENDS = ""
