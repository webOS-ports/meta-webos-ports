#!/bin/sh
# Wait for the modem's IRadio slots to register on /dev/hwbinder before ofonod
# starts.
#
# ofono-binder-plugin enumerates its slots ONCE at startup and never retries.
# The vendor RIL lives in the Android container and registers asynchronously,
# so on a cold boot ofono can win the race and create only the slots that
# happen to be up. On radon (dual SIM, both cards inserted) that meant exactly
# one modem at every boot:
#
#   /etc/ofono/binder.conf   [slot1] and [slot2], both written before ofono ran
#   binder-list              IRadio/slot1 AND IRadio/slot2 registered
#   ofono at boot            only /ril_0
#   systemctl restart ofono  /ril_0 AND /ril_1, from the very same files
#
# so the user was prompted for one SIM PIN instead of two. Ordering ofono after
# luneos-device-config fixed the *config* race - the two-slot binder.conf is
# now always in place first - but not the *registration* race behind it.
#
# FuriLabs solve this on the same hardware with
#   ExecStartPre=/usr/bin/binder-wait android.hardware.radio@1.0::IRadio/slot1
# in their own ofono drop-in. binder-wait is a FuriLabs tool and not in
# upstream libgbinder, so this does the same job with binder-list, which is.
#
# The slot list is READ FROM binder.conf rather than hardcoded: that file is
# generated per device from the HAL's own advertised slots (or from
# deviceinfo_binder_slots), so this stays correct on a single-SIM device, on a
# device with no modem at all, and on anything with more than two slots.
#
# Always exits 0. A modem that never registers must not wedge the boot - ofono
# will simply come up with whatever is there, which is what happens today.
CONF=/etc/ofono/binder.conf
TIMEOUT=${OFONO_BINDER_WAIT_TIMEOUT:-30}

command -v binder-list >/dev/null 2>&1 || exit 0
[ -r "$CONF" ] || exit 0

# "[slot1]" / "[slot2]" section headers are the slot names the plugin will use.
slots=$(sed -n 's/^\[\(slot[0-9]\+\)\].*/\1/p' "$CONF" | sort -u)
[ -n "$slots" ] || exit 0

i=0
while [ "$i" -lt "$TIMEOUT" ]; do
    have=$(binder-list -d /dev/hwbinder 2>/dev/null |
           grep -oE 'IRadio/slot[0-9]+' | sed 's|.*/||' | sort -u)
    missing=
    for s in $slots; do
        echo "$have" | grep -qx "$s" || missing="$missing $s"
    done
    if [ -z "$missing" ]; then
        echo "ofono-binder-wait: all slots registered:$(echo $slots | sed 's/^/ /')"
        exit 0
    fi
    sleep 1
    i=$((i + 1))
done

echo "ofono-binder-wait: giving up after ${TIMEOUT}s; still missing:$missing"
exit 0
