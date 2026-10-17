# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "Office document conversion service (LibreOfficeKit)"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# libreoffice for the LibreOfficeKit headers (libreoffice-lok-dev) at build
# time; office-convert dlopen()s LibreOffice itself, so the run-time
# dependency is explicit.
DEPENDS = "luna-service2 glib-2.0 libpbnjson libreoffice"
RDEPENDS:${PN} = "libreoffice"

PV = "1.0.0-1+git"

# Pinned, never AUTOREV: see org.webosports.service.torch.bb.
SRCREV = "0fc6045638e34d7349f7523568ab6e6e3395bc9b"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# A dynamic service (Type=dynamic in its .service file): the hub starts it on
# the first call and it exits when idle, so there is no systemd unit.
FILES:${PN} += "${webos_sysbus_datadir} ${libexecdir}/office-convert"
