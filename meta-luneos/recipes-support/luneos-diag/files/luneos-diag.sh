#!/bin/sh
# Boot-time state dump. Not board-specific: everything here is generic Linux
# sysfs, so it is as useful on a Halium device as on a mainline one.
#
# The kernel log tells us what failed loudly, but the things that keep biting us
# (dwc3 idle vs never bound, which DRM node a client opened, whether the regdb
# loaded) are silent on success and only visible in sysfs. Dump them once at
# boot so a single SD-card read answers the question instead of a round trip.
#
# Everything here is BusyBox-safe: POSIX sh, "head -n N", no arrays, no bashisms.

# Normally a one-shot boot dump. When triggered by a udev typec/power-supply
# event we append to a separate file instead, so a plug/unplug that happens
# long after boot is actually captured - the boot dump fires at +25s and kept
# missing the moment the cable went in.
OUT=${LUNEOS_DIAG_OUT:-/var/log/luneos-diag.txt}
# Keep the event file bounded. udev triggers a dump on every power_supply
# "change", and on an Android kernel those fire every few seconds while
# charging (current, charge level, temperature all raise one), so an unbounded
# append would grow for as long as the cable is in. Keep the newest
# LUNEOS_DIAG_MAX_BYTES and drop the rest; the recent dumps are the useful ones.
LUNEOS_DIAG_MAX_BYTES=${LUNEOS_DIAG_MAX_BYTES:-262144}

# And do not re-dump more often than once a minute.
#
# The size cap above bounds the file, but not the work: every power_supply
# "change" still meant a 3 s sleep and ~50 sysfs reads, and on an Android kernel
# charging raises one every few seconds. Nothing is learned from the fiftieth
# dump of a steady charge, so drop events that arrive inside the window - the
# boot dump is never throttled, only the udev-triggered ones.
#
# Uptime rather than date(1) or stat(1): it is a monotonic integer, needs no
# coreutils, and cannot be upset by the clock being set during boot.
LUNEOS_DIAG_MIN_INTERVAL=${LUNEOS_DIAG_MIN_INTERVAL:-60}
if [ "$LUNEOS_DIAG_APPEND" = "1" ]; then
    _now=$(cut -d' ' -f1 /proc/uptime 2>/dev/null | cut -d. -f1)
    _stamp=${OUT}.last
    _prev=$(cat "$_stamp" 2>/dev/null || echo "")
    if [ -n "$_now" ] && [ -n "$_prev" ] &&
       [ "$((_now - _prev))" -lt "$LUNEOS_DIAG_MIN_INTERVAL" ] 2>/dev/null; then
        exit 0
    fi
    [ -n "$_now" ] && echo "$_now" > "$_stamp" 2>/dev/null
fi

if [ "$LUNEOS_DIAG_APPEND" = "1" ] && [ -f "$OUT" ]; then
    _sz=$(wc -c < "$OUT" 2>/dev/null || echo 0)
    if [ "$_sz" -gt "$LUNEOS_DIAG_MAX_BYTES" ] 2>/dev/null; then
        tail -c "$LUNEOS_DIAG_MAX_BYTES" "$OUT" > "$OUT.tmp" 2>/dev/null &&
            mv "$OUT.tmp" "$OUT"
    fi
fi

if [ "$LUNEOS_DIAG_APPEND" = "1" ]; then
    exec >>"$OUT" 2>&1
    echo; echo "############################################################"
    echo "### event dump: $(date 2>/dev/null) uptime $(cut -d' ' -f1 /proc/uptime)s"
    echo "###   ACTION=$ACTION SUBSYSTEM=$SUBSYSTEM DEVPATH=$DEVPATH"
    echo "############################################################"
else
    exec >"$OUT" 2>&1
fi

say() { echo; echo "=================== $* ==================="; }
cat_if() { [ -e "$1" ] && { printf '%s = ' "$1"; cat "$1" 2>/dev/null; } || echo "$1 = <absent>"; }

# dwc3 dev_dbg, opt-in only:
#
#   LUNEOS_DIAG_DWC3_DEBUG=1 luneos-diag.sh
#
# dwc3 is built in, so its dev_dbg cannot be enabled with modprobe; dynamic
# debug is the way in. This used to be switched on unconditionally, on the
# grounds that it "costs nothing when the port is idle" - which is true only
# while the port is idle. Once a cable is in, dwc3 prints per interrupt and per
# endpoint event, and it is never switched back off, so the flood lasts the rest
# of the boot.
#
# That was acceptable while this recipe was pinned to three Pine64 boards and
# existed to chase one gadget bug (connects at high speed, never answers ep0).
# It is not acceptable now that it is recommended on every machine: dwc3 is the
# controller on most Qualcomm and MediaTek SoCs too, so a Halium phone left
# charging would log continuously for no reason.
if [ "${LUNEOS_DIAG_DWC3_DEBUG:-0}" = "1" ] &&
   [ -w /sys/kernel/debug/dynamic_debug/control ]; then
    echo "module dwc3 +p" > /sys/kernel/debug/dynamic_debug/control 2>/dev/null &&
        echo "luneos-diag: dwc3 dynamic debug enabled (LUNEOS_DIAG_DWC3_DEBUG=1)"
fi

echo "luneos-diag  $(uname -srm)  booted $(cat /proc/uptime | cut -d' ' -f1)s ago"
echo "machine: $(cat /etc/hostname 2>/dev/null)"

say "USB: dwc3 / UDC / gadget"
echo "-- /sys/class/udc (empty means dwc3 never registered a gadget) --"
ls -l /sys/class/udc/ 2>/dev/null || echo "  <none>"
for u in /sys/class/udc/*; do
    [ -e "$u" ] || continue
    echo "-- $u --"
    for f in state current_speed function is_otg is_a_peripheral soft_connect; do
        cat_if "$u/$f"
    done
done
echo "-- dwc3 platform devices --"
ls -d /sys/bus/platform/drivers/dwc3/* 2>/dev/null || echo "  dwc3 driver has NO bound devices"
ls -d /sys/bus/platform/drivers/dwc3-of-simple/* 2>/dev/null || echo "  dwc3-of-simple has NO bound devices"
echo "-- dwc3 mode (debugfs) --"
for m in /sys/kernel/debug/usb/dwc3*/mode /sys/kernel/debug/usb/*.usb/mode; do
    [ -e "$m" ] && cat_if "$m"
done

echo "-- dwc3 controller state --"
# Does dwc3 think it has connected? link_state shows the USB link state
# machine; on a peripheral that never sees a host it stays disconnected even
# though the gadget is bound and VBUS is present. testmode and the DCTL
# run/stop bit say whether the pullup was ever asserted.
for d in /sys/kernel/debug/usb/*.usb /sys/kernel/debug/usb/dwc3*; do
    [ -d "$d" ] || continue
    echo "---- $d ----"
    echo "   files: $(ls "$d" 2>/dev/null | tr '\n' ' ')"
    for f in link_state testmode mode current_speed maximum_speed; do
        [ -e "$d/$f" ] && printf '   %-14s = %s\n' "$f" "$(cat "$d/$f" 2>&1 | head -n 1)"
    done
    # DCTL bit31 = RUN_STOP (the D+ pullup). Present only on kernels that
    # expose a register dump.
    if [ -e "$d/regdump" ]; then
        echo "   DCTL/GCTL:"
        grep -iE "^(DCTL|GCTL|DSTS|GSTS)" "$d/regdump" 2>/dev/null | head -n 6 | sed 's/^/     /'
    fi
done
echo "-- configfs gadget --"
ls -l /sys/kernel/config/usb_gadget/ 2>/dev/null || echo "  <no gadget configured>"
for g in /sys/kernel/config/usb_gadget/*; do
    [ -d "$g" ] || continue
    cat_if "$g/UDC"
    ls "$g/functions" 2>/dev/null
done

say "USB-C: typec ports, roles, power"
for p in /sys/class/typec/*; do
    [ -e "$p" ] || continue
    echo "-- $p --"
    for f in data_role power_role port_type orientation vconn_source usb_power_delivery_revision; do
        cat_if "$p/$f"
    done
done
[ -d /sys/class/typec ] || echo "  /sys/class/typec absent - no typec port registered"
echo "-- TCPM state machine (debugfs) --"
# TCPM logs its state transitions here, not to dmesg. This is what tells us
# whether the port is toggling as a DRP or parked in SRC_UNATTACHED, and what
# it reads on CC1/CC2.
for t in /sys/kernel/debug/tcpm/* /sys/kernel/debug/usb/tcpm-*; do
    [ -e "$t" ] || continue
    echo "---- $t ----"
    tail -n 60 "$t" 2>/dev/null
done
[ -e /sys/kernel/debug/tcpm ] || echo "  <no tcpm debugfs>"

echo "-- Type-C chip interrupt (is it firing at all?) --"
# If this count never moves across a plug event, the RT1711H is not
# interrupting and TCPM can never learn about the attach.
grep -iE "rt1711|husb|tcpc|typec|0-004e" /proc/interrupts 2>/dev/null || echo "  <no matching irq line>"
echo "-- USB controller interrupts (is dwc3 getting events?) --"
# link_state can read "On" with the gadget stack still at NOTATTACHED. If the
# dwc3 IRQ count is 0 the controller is up but delivering no events, so ep0
# never answers the host's SETUP and enumeration dies after connect.
grep -iE "dwc3|xhci|fcc00000|fd000000|usb" /proc/interrupts 2>/dev/null | head -n 10 || echo "  <none>"
echo "-- gpio0 (RK_PC5 is the usbcc int line) --"
grep -iE "gpio-|usbcc|bes2600|wlan" /sys/kernel/debug/gpio 2>/dev/null | head -n 25 || echo "  <no debugfs gpio>"

echo "-- usb role switches --"
for r in /sys/class/usb_role/*; do cat_if "$r/role"; done
echo "-- extcon --"
for e in /sys/class/extcon/*; do
    echo "-- $e ($(cat $e/name 2>/dev/null)) --"
    for s in $e/cable.*; do
        [ -e "$s" ] && echo "   $(cat $s/name 2>/dev/null) = $(cat $s/state 2>/dev/null)"
    done
done

say "Power supplies / battery"
for b in /sys/class/power_supply/*; do
    echo "-- $b ($(cat $b/type 2>/dev/null)) --"
    for f in present status online capacity voltage_now current_now health; do
        [ -e "$b/$f" ] && printf '   %s = %s\n' "$f" "$(cat $b/$f 2>/dev/null)"
    done
done

say "DRM / display"
for c in /sys/class/drm/*; do
    n=$(basename "$c")
    case "$n" in
        card*-*) printf '  %-28s status=%s\n' "$n" "$(cat $c/status 2>/dev/null)";;
        card*|renderD*) printf '  %-28s driver=%s\n' "$n" "$(basename $(readlink -f $c/device/driver) 2>/dev/null)";;
    esac
done
echo "-- /dev/dri --"
ls -l /dev/dri/ 2>/dev/null

say "GL / EGL (what a client actually gets)"
echo "-- libEGL wayland-display support --"
for l in /usr/lib/libEGL.so.1 /usr/lib/libEGL_mesa.so.0; do
    [ -e "$l" ] || continue
    if strings "$l" 2>/dev/null | grep -q wl_drm; then
        echo "  $l: wl_drm PRESENT (EGL_WL_bind_wayland_display usable)"
    else
        echo "  $l: wl_drm MISSING -> compositor wayland-egl integration will fail"
    fi
done
echo "-- Qt wayland integrations installed --"
ls /usr/lib/plugins/wayland-graphics-integration-server/ 2>/dev/null
# One "ls -l" per process, not one "readlink" per descriptor.
#
# This used to readlink every fd of every process, which is a fork per
# descriptor: measured at 27 s of the script's 29 s total, and it scales with
# how busy the device is, so the worst case is a loaded phone rather than an
# idle one. One listing per process is the same information for roughly a
# hundredth of the forks - 0.2 s here.
echo "-- who has the GPU open --"
for p in /proc/[0-9]*; do
    _hits=$(ls -l "$p"/fd 2>/dev/null | grep "/dev/dri/" || true)
    [ -n "$_hits" ] || continue
    _comm=$(cat "$p/comm" 2>/dev/null)
    _pid=${p#/proc/}
    echo "$_hits" | while read -r _l; do
        case "$_l" in
            *"/dev/dri/"*) echo "  $_comm ($_pid) -> /dev/dri/${_l##*/dev/dri/}" ;;
        esac
    done
done 2>/dev/null | sort -u

say "WiFi / regulatory"
cat_if /sys/module/cfg80211/parameters/ieee80211_regdom
echo "-- regdb files --"
for f in /lib/firmware/regulatory.db /lib/firmware/regulatory.db.p7s /usr/lib/crda/regulatory.bin; do
    [ -e "$f" ] && echo "  $f  ($(wc -c < $f) bytes)" || echo "  $f  <absent>"
done
command -v iw >/dev/null && { echo "-- iw reg get --"; iw reg get; }

echo "-- brcmfmac: which firmware/NVRAM actually loaded --"
# The firmware banner is the single most useful WiFi fact and it is easy to
# miss in the journal, so pull it from every place it is exposed.
dmesg 2>/dev/null | tr -d '\000' | grep -i brcmf | head -n 40
command -v ethtool >/dev/null && ethtool -i wlan0 2>/dev/null
for r in /sys/kernel/debug/brcmfmac/*/revinfo; do cat_if "$r"; done
echo "-- brcm modules loaded (7.x split the driver per vendor) --"
lsmod 2>/dev/null | grep -iE "brcm|cfg80211" || echo "  <lsmod unavailable>"
echo "-- firmware files present for this chip --"
ls -la /lib/firmware/brcm/ 2>/dev/null | grep -iE "4345|4335|4356|4359"

echo "-- link state and AP capabilities --"
command -v iw >/dev/null && {
    iw dev wlan0 link 2>/dev/null
    iw dev wlan0 info 2>/dev/null
}
# RSN/PMF of the AP we keep failing on: a WPA3 / PMF-required AP is a classic
# cause of "associates, then the 4-way never happens".
command -v wpa_cli >/dev/null && {
    echo "-- wpa_cli status --"
    wpa_cli -i wlan0 status 2>/dev/null
    echo "-- wpa_cli scan_results (flags column shows WPA2/WPA3/PMF) --"
    wpa_cli -i wlan0 scan_results 2>/dev/null | head -n 25
}
echo "-- interfaces --"
ip -o link 2>/dev/null | sed 's/^/  /'

say "Deferred probes still pending"
cat /sys/kernel/debug/devices_deferred 2>/dev/null || echo "  <debugfs not mounted>"

say "Firmware load failures this boot"
dmesg 2>/dev/null | tr -d '\000' | grep -i "firmware" | grep -iE "fail|not found" | head -n 30

say "done"
