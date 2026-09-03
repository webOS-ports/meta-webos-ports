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

# MediaTek CCCI (the AP<->modem link on MTK Halium devices) exposes a "LAN"
# netdev alongside the numbered cellular-data ones: ccmni0, ccmni1, ... plus a
# trailing ccmni-lan. connman has no idea what that is, so it falls through to
# matching it as plain Ethernet and brings it up as a "Wired" service.
#
# That triggers a livelock in the vendor driver's RX worker for it
# (ccmniNN_rx_q_worker, NN = ccmni-lan's ifindex): recv_from_port_list() spins
# forever with the interface reporting rx=0 the entire time, running one CPU
# core flat out from ~24s after boot onward. Confirmed on mindphone by sampling
# workqueue_execute_start/end over tracefs -- zero events while the worker's
# stime kept climbing, i.e. it never returns from the one work item it started
# on, rather than looping over many.
#
# It also makes the interface undownable, which is worse than the wasted core:
# ccmni_close() calls flush_delayed_work() on that same stuck worker, and
# rtnetlink holds RTNL for the whole ndo_stop call. So anything that closes
# ccmni-lan afterwards -- an "ip link set down", restarting connman, a normal
# reboot's teardown path -- blocks forever holding RTNL, which then wedges
# every other process trying to touch network configuration (verified:
# connmand itself ended up stuck in D state on rtnl_lock behind it).
#
# connman's blacklist is prefix-matched, so this intentionally names ccmni-lan
# exactly rather than the ccmni prefix: ccmni0/ccmni1 are the actual cellular
# data interfaces the modem stack uses and must stay usable.
#
# mindphone (MT6739) is the only MTK machine this layer builds today, so this
# is scoped to it directly rather than to a SoC-family override that does not
# exist yet. If a second MTK device shows up, promote this to a MACHINEOVERRIDES
# family override (:mtk or similar) the way :halium already works above.
do_install:append:mindphone() {
    sed -i 's/^\(NetworkInterfaceBlacklist = .*\)$/\1,ccmni-lan/' \
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
