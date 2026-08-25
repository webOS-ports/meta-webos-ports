# Copyright (c) 2017-2024 LG Electronics, Inc.

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

EXTENDPRAUTO:append = "webos4"

SRC_URI += " \
    file://main.conf \
"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "connman.service"
WEBOS_SYSTEMD_SCRIPT = "connman.sh.in"
WEBOS_SYSTEMD_REPLACE_OTHERS = "-DWEBOS_CONNMAN_PREACTIVATE_INTERFACE_LIST="${WEBOS_CONNMAN_PREACTIVATE_INTERFACE_LIST}" -DDATADIR="${datadir}""

do_install:append() {
    install -d ${D}${sysconfdir}/connman
    install -m 0644 ${UNPACKDIR}/main.conf ${D}${sysconfdir}/connman/
}

# Android WiFi HALs expose more than the one station interface LuneOS uses.
# On a Pixel 3a the driver registers wlan0, a second station interface wlan1,
# and the WiFi Direct interface p2p0, and connman happily takes all three into
# its wifi technology:
#
#   Interfaces = [ wlan0, wlan1, p2p0 ]
#
# wlan1 then scans alongside wlan0, so every access point is discovered twice -
# once per MAC - and every consumer reading connman's service list directly
# shows each network twice. (webos-connman-adapter's findnetworks collapses
# them by SSID, which is why this only shows up in the QML settings apps.)
#
# LuneOS uses neither interface: the P2P half of the webOS connman fork was not
# ported to 2.0, and nothing drives a second station. Keep connman off both.
# The list is prefix matched, and it replaces the built-in default rather than
# extending it, so this appends to what main.conf already sets.
do_install:append:halium() {
    sed -i 's/^\(NetworkInterfaceBlacklist = .*\)$/\1,wlan1,p2p0/' \
        ${D}${sysconfdir}/connman/main.conf
}

FILES:${PN} += " \
    ${sysconfdir}/connman \
    ${sysconfdir} \
"

# main.conf turns the online check on, and connman resolves the proxy for the
# check URL by asking pacrunner over D-Bus. With no pacrunner on the bus the
# lookup fails outright - "no valid proxy", even on a service with no proxy
# configured at all - and after six failures the service drops to "ready" with
# Error=online-check-failed and never reaches "online". Recommends rather than
# depends: connman itself is fine without it, this config is not.
RRECOMMENDS:${PN} += "pacrunner"
