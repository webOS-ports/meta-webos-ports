# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "Hardware privacy switch service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# No nyx: everything device-specific lives in the config file, not in a module.
DEPENDS = "luna-service2 glib-2.0 libpbnjson"

PV = "1.0.0-1+git"

# Pinned, never AUTOREV. webos_ports_repo resolves AUTOREV with a git ls-remote
# at PARSE time, so a repo that is unreachable halts parsing for the whole
# layer rather than failing this one recipe - see the same note in
# org.webosports.service.torch.bb.
SRCREV = "f1fbf004ed9738daf9750212cd70f2370ad35170"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_daemon
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# As in the torch recipe: webos_build_daemon(LAUNCH files/launch) in
# CMakeLists.txt already installs the unit, so webos_systemd would go looking
# for a second copy under files/systemd that does not exist. systemd.bbclass is
# inherited only to enable the unit that is already installed.
SYSTEMD_SERVICE:${PN} = "killswitchd.service"

# The switches a device has, and how to read each one, are per machine - so the
# package is too. A machine with no config file of its own still gets the
# daemon, which finds nothing to report and exits immediately; the unit's
# ConditionPathExists means systemd does not even start it.
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install:append() {
    if [ -f ${S}/conf/killswitchd.conf.${MACHINE} ]; then
        install -d ${D}${sysconfdir}
        install -m 0644 ${S}/conf/killswitchd.conf.${MACHINE} ${D}${sysconfdir}/killswitchd.conf
    else
        bbnote "no killswitchd.conf.${MACHINE}; ${MACHINE} declares no hardware switches"
    fi
}

FILES:${PN} += "${sysconfdir}/systemd/system/killswitchd.service"
FILES:${PN} += "${sysconfdir}/killswitchd.conf"
FILES:${PN} += "${webos_sysbus_datadir}"
FILES:${PN} += "${libexecdir}/killswitchd"

# The camera helper waits for the sensor to become openable by probing with a
# minimal droidcamsrc pipeline, which needs gst-launch-1.0. Recommended rather
# than required: the helper checks for it and returns early without it, so an
# image that leaves gstreamer tools out still works - it just goes back to the
# camera app blocking for the eight or nine seconds the sensor takes, with its
# UI frozen, which is the whole thing the wait exists to avoid.
RRECOMMENDS:${PN} += "gstreamer1.0"

