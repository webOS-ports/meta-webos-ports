#!/bin/sh
# tissot-wcnss-filter-fixup
#
# /system/vendor/bin/wcnss_filter (init service "vendor.start_hci_filter") is
# redundant on this platform and degenerates into a busy loop.
#
# On stock QCOM/WCNSS, wcnss_filter owns the BT/FM/ANT SoC channel and the BT HAL
# talks to it over @bt_sock. Here the A9 vendor HAL
# (android.hardware.bluetooth@1.0-service-qti) opens /dev/smd2 + /dev/smd3
# *directly*, and LuneOS reaches it through bluebinder -> /dev/vhci -> hci0.
# libbt-vendor still ctl.start's the filter during BT power-on, but the SMD
# channels are already taken, so its SoC open fails and handle_soc_events()
# spins on the dead fd forever instead of exiting:
#
#   E WCNSS_FILTER: handle_soc_events: Unexpected data format!!:55 - Ignore the Packet
#
# Measured cost when left running: ~96% of one CPU core in wcnss_filter plus
# ~36% of a core in logd absorbing ~62000 log lines/s (6.6 MB/s), which also
# pins both clusters at max frequency and blocks suspend.
#
# Verified: with the filter stopped, BT survives a full cold re-init
# (HAL restart + bluebinder restart + hci0 up) and inquiry scans work.
#
# Idempotent; no-ops once the vendor image / init rc stops starting the filter.

set -u

TAG=tissot-wcnss-filter-fixup
SETPROP=/usr/bin/setprop

stop_filter() {
    [ -x "$SETPROP" ] && "$SETPROP" ctl.stop vendor.start_hci_filter 2>/dev/null
    # init may not own it (libbt-vendor can spawn it directly) - fall back to a signal
    i=0
    while [ $i -lt 5 ]; do
        pidof wcnss_filter >/dev/null 2>&1 || return 0
        pkill -x wcnss_filter 2>/dev/null
        sleep 1
        i=$((i + 1))
    done
    pidof wcnss_filter >/dev/null 2>&1 && return 1
    return 0
}

if pidof wcnss_filter >/dev/null 2>&1; then
    logger -t "$TAG" "wcnss_filter is running - stopping it (redundant here, busy-loops at ~96% CPU)"
    if stop_filter; then
        logger -t "$TAG" "wcnss_filter stopped"
    else
        logger -t "$TAG" "WARNING: could not stop wcnss_filter"
    fi
else
    logger -t "$TAG" "wcnss_filter not running at startup - nothing to do"
fi

# Guard: libbt-vendor re-issues ctl.start on some BT power-on paths.
# A 30s poll is free compared with a core spinning at 96%.
while true; do
    sleep 30
    if pidof wcnss_filter >/dev/null 2>&1; then
        logger -t "$TAG" "wcnss_filter reappeared - stopping it again"
        stop_filter || logger -t "$TAG" "WARNING: could not stop wcnss_filter"
    fi
done
