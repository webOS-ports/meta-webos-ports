SUMMARY = "Screen-on/off power tuning for Halium devices (LuneOS daemon for libbatman)"
DESCRIPTION = "Drives libbatman, FuriLabs batman's tuning engine, from \
luna://com.palm.display/control/status, batteryd's charger signals and \
audiod's stream status. While the display is off it switches CPU, GPU and \
devfreq governors to powersave and sends the Android power HAL the hints the \
framework sends at screen off (setInteractive(false) / low-power mode). It \
works while the phone is awake and does not suspend it; sleepd still does that."
SECTION = "webos/services"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "glib-2.0 batman luna-service2 libpbnjson"

PV = "1.0+git"
PR = "r0"

SRCREV = "6f0d05ce7d6b796a752a7b1565c61367e1777a4a"

inherit webos_ports_repo
WEBOS_GIT_PARAM_BRANCH = "master"
WEBOS_REPO_NAME = "luna-batman"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit meson pkgconfig systemd

EXTRA_OEMESON = "-Dsystemdsystemunitdir=${systemd_system_unitdir}"

COMPATIBLE_MACHINE = "(halium)"

# batman-luna (the daemon) used to be built and packaged by the batman recipe.
RREPLACES:${PN} = "batman"
RCONFLICTS:${PN} = "batman"

FILES:${PN} += " \
    ${systemd_system_unitdir}/batman-luna.service \
    ${datadir}/luna-service2 \
"
CONFFILES:${PN} = "${sysconfdir}/batman/config"

SYSTEMD_SERVICE:${PN} = "batman-luna.service"
SYSTEMD_AUTO_ENABLE = "enable"
