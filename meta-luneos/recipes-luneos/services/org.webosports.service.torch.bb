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
SRCREV = "38bb81e"  # NOT YET PUSHED - do_fetch fails until it is

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_daemon
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://org.webosports.service.torch-clients.perm.json \
"

# Deliberately NOT using webos_systemd/LUNEOS_SYSTEMD_SERVICE. That class copies
# from ${S}/files/systemd/<name>, whereas webos_build_daemon(LAUNCH files/launch)
# in CMakeLists.txt already installs the unit from files/launch/torchd.service.in.
# Declaring both meant install_units looked for a second copy under files/systemd
# that does not exist, and do_install failed.
#
# Consequence worth knowing: the unit is installed but not enabled at boot, so
# torchd must be started by hand (or socket-activated by luna-service2) for now.
# The tidy fix is to move the unit to files/systemd/torchd.service.in in the
# service repo and use LUNEOS_SYSTEMD_SERVICE, as com.webos.service.battery does.
# Enable at boot. Without this the unit is installed but never started, so after
# a reboot getStatus has nobody to answer and the menu reads "Unavailable" -
# indistinguishable from the device having no torch at all.
SYSTEMD_SERVICE:${PN} = "torchd.service"

# cardshell may only call the groups listed for com.webos.surfacemanager* in
# client-permissions.d, and torch.operation is not among those luna-surfacemanager
# ships - so its subscribe is rejected and the menu shows "Unavailable". Granting
# it from here keeps the grant with the package that defines the group, survives
# image updates, and avoids editing a file another package owns.
do_install:append() {
    install -d ${D}${webos_sysbus_permissionsdir}
    install -m 0644 ${UNPACKDIR}/org.webosports.service.torch-clients.perm.json \
        ${D}${webos_sysbus_permissionsdir}/
}

FILES:${PN} += "${sysconfdir}/systemd/system/torchd.service"

FILES:${PN} += "${webos_sysbus_datadir}"
