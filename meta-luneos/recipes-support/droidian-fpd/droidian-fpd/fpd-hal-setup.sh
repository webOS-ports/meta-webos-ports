#!/bin/sh
# Bring up the vendor fingerprint HAL for droidian-fpd.
#
# Two device-side prerequisites have to be met before the daemon can talk to
# the sensor, and neither happens on its own inside the Halium container:
#
#  1. Keymaster-4 devices (e.g. sargo) need the gatekeeper/keymaster HMAC key
#     agreement that Android's keystored normally performs at boot. Without it
#     the fingerprint TA refuses to mint auth tokens and fps_hal dies with
#     "KEYMASTER_GET_AUTH_TOKEN_KEY returned status=-24 / fpc_hal_open failed".
#     fake_crypt performs that agreement; it is built into the Android image
#     (PRODUCT_PACKAGES) and run inside the container.
#
#  2. The vendor fingerprint HAL is usually an init "class late_start" service,
#     which the stripped-down container init never triggers, so it must be
#     asked to start explicitly.
#
# Everything here is best-effort and bounded. On a device that needs no
# fake_crypt, or where any step times out, we still exit 0: this runs as the
# daemon's ExecStartPre and must never block or fail the daemon start. If the
# HAL still is not up, droidian-fpd's own getService() retry and Restart=always
# remain the backstop.

log() { echo "fpd-hal-setup: $*"; }

# Locate fake_crypt inside the container: the GSI ships it in /system/bin; a
# hand-placed copy under /data/local is the fallback used before a rebuilt
# image is flashed.
FAKE_CRYPT=
for p in /system/bin/fake_crypt /data/local/fake_crypt; do
    if lxc-attach -n android -- test -x "$p" 2>/dev/null; then
        FAKE_CRYPT=$p
        break
    fi
done

# Wait (bounded, ~10s) for a keymaster HAL so the HMAC agreement can succeed.
i=0
while [ $i -lt 50 ]; do
    if getprop | grep -E '^\[init\.svc\.(vendor\.)?keymaster' | grep -q '\[running\]'; then
        break
    fi
    i=$((i + 1))
    sleep 0.2
done

if [ -n "$FAKE_CRYPT" ]; then
    # keymaster can need a moment after it reports running before it will
    # answer the agreement, so retry until it reports success (bounded ~30s).
    i=0
    while [ $i -lt 30 ]; do
        out=$(lxc-attach -n android -- "$FAKE_CRYPT" 2>&1)
        log "$out"
        if echo "$out" | grep -q "HMAC sharing agreement complete"; then
            break
        fi
        i=$((i + 1))
        sleep 1
    done
else
    log "fake_crypt not found in container; assuming device needs no keymaster shim"
fi

# Ask init to start the vendor fingerprint HAL.
setprop ctl.start vendor.fps_hal

# Wait (bounded, ~10s) until it is actually running, so the daemon connects
# straight away instead of sitting in its getService() retry until systemd's
# start timeout fires.
i=0
while [ $i -lt 50 ]; do
    if [ "$(getprop init.svc.vendor.fps_hal)" = "running" ]; then
        log "vendor.fps_hal running"
        break
    fi
    i=$((i + 1))
    sleep 0.2
done

exit 0
