# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "E Ink refresh-mode service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 glib-2.0 libpbnjson"

PV = "1.0.0-1+git"

# Pinned, never AUTOREV - see org.webosports.service.torch for why.
SRCREV = "9767aefe6225827981d328781f638f4d4694ee3d"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_daemon
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# webos_build_daemon(LAUNCH files/launch) in CMakeLists.txt installs the unit
# from files/launch/einkd.service.in; systemd.bbclass is inherited only to
# enable it. Same arrangement as torchd, and for the same reason: without
# SYSTEMD_SERVICE the unit ships but never starts, and the Settings app then
# cannot tell "no E Ink panel" from "nobody answering".
SYSTEMD_SERVICE:${PN} = "einkd.service"

# Not in a packagegroup: the panel is machine-specific, so the machine that has
# it (mp01.conf) pulls this in through MACHINE_EXTRA_RRECOMMENDS. The client
# grant the shell will need (eink.operation for com.webos.surfacemanager*)
# ships from the component itself; the Settings app asks for the group in its
# own appinfo.

FILES:${PN} += "${sysconfdir}/systemd/system/einkd.service"

FILES:${PN} += "${webos_sysbus_datadir}"
