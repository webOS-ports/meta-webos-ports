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

SYSTEMD_SERVICE:${PN} = "einkd.service"

FILES:${PN} += "${sysconfdir}/systemd/system/einkd.service"

FILES:${PN} += "${webos_sysbus_datadir}"
