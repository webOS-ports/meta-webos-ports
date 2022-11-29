# Copyright (c) 2015-2018 LG Electronics, Inc.

SUMMARY = "Event Monitoring Service for generic notifications"
AUTHOR = "Viesturs Zarins <viesturs.zarins@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 pmloglib libpbnjson libwebosi18n"

PV = "1.1.0-13+git${SRCPV}"
SRCREV = "89cffbb2fb8b1984ca09a9d25dc13e0557a6e0e4"

inherit webos_cmake
inherit webos_system_bus
inherit webos_event_monitor_plugin
inherit webos_public_repo
inherit webos_systemd
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-event-monitor-Add-systemd-service-file.patch \
file://0002-Add-trustLevel.patch \
"

S = "${WORKDIR}/git"

#Enable/disable mock plugin
EXTRA_OECMAKE += "-DBUILD_MOCK_PLUGIN:BOOL='NO'"

#webos_event_monitor_plugin depends on event-monitor, remove circular dependency
WEBOS_EVENT_MONITOR_PLUGIN_DEPENDS = ""
