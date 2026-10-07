# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "Infrared transmitter (IR blaster) service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# Talks to the hardware itself - Samsung's sec_ir sysfs node or /dev/lirc0 -
# so no nyx module and nothing from the Android side.
DEPENDS = "luna-service2 glib-2.0 libpbnjson"

PV = "1.0.0-1+git"

# Pinned, never AUTOREV: see org.webosports.service.torch.bb.
SRCREV = "1b485ce02b840bef449cad20ae53828f18b67804"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_daemon
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# As for torchd: webos_build_daemon() installs the unit from files/launch, and
# systemd.bbclass is only here to enable it.
SYSTEMD_SERVICE:${PN} = "irblasterd.service"

FILES:${PN} += "${sysconfdir}/systemd/system/irblasterd.service"
FILES:${PN} += "${webos_sysbus_datadir}"
