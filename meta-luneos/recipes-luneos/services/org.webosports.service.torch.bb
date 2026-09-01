# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "Torch (flashlight) service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# nyx-lib rather than nyx-modules: everything device-specific is behind
# NYX_DEVICE_LED "Torch", which resolves at runtime to whichever module the
# machine built - the sysfs one or the hybris camera-service one.
DEPENDS = "nyx-lib luna-service2 glib-2.0 libpbnjson"

PV = "1.0.0-1+git"

# Pinned, never AUTOREV. webos_ports_repo resolves AUTOREV with a git ls-remote
# at PARSE time, so a repo that is unreachable - or, as happened here, not yet
# pushed - halts parsing for the entire layer rather than failing this one
# recipe, taking every other build sharing that layer with it.
SRCREV = "d447e6f92b1a46b8597e93571c33ace45b7760a5"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_daemon
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# Deliberately NOT using webos_systemd/LUNEOS_SYSTEMD_SERVICE. That class copies
# from ${S}/files/systemd/<name>, whereas webos_build_daemon(LAUNCH files/launch)
# in CMakeLists.txt already installs the unit from files/launch/torchd.service.in.
# Declaring both meant install_units looked for a second copy under files/systemd
# that does not exist, and do_install failed.
#
# systemd.bbclass is inherited purely to enable that already-installed unit.
# Without SYSTEMD_SERVICE the unit ships but is never started, so after a reboot
# getStatus has nobody to answer and the menu reads "Unavailable" - which is
# indistinguishable from the device having no torch at all.

SYSTEMD_SERVICE:${PN} = "torchd.service"

# cardshell may only call the groups listed for com.webos.surfacemanager* in
# client-permissions.d, and torch.operation is not among those luna-surfacemanager
# ships - so its subscribe is rejected and the menu reads "Unavailable", which is
# indistinguishable from the device having no torch. That grant now ships from the
# component itself, as files/sysbus/org.webosports.service.torch-clients.perm.json.in,
# which webos_build_system_bus_files() installs along with the rest of the sysbus
# files - so there is nothing to do here.

FILES:${PN} += "${sysconfdir}/systemd/system/torchd.service"

FILES:${PN} += "${webos_sysbus_datadir}"
