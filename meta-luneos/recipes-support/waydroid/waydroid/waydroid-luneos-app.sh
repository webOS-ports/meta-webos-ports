#!/bin/sh
# Launch an Android application from a webOS launcher tile and stay in the
# foreground for as long as Android has something on screen.
#
#   waydroid-luneos-app             show the Android full UI (app id Waydroid)
#   waydroid-luneos-app <package>   show one app     (app id waydroid.<package>)
#
# Being long-running is the point. SAM tracks a native app by the process it
# spawned, so a script that asked Waydroid to start something and exited was
# never "running": the launcher never marked the app open and
# com.webos.applicationManager/close answered "is not running" while the card
# sat there. Staying alive also gives closing somewhere to land, because SAM
# closes a native app by signalling its process.
#
# The app ids are not free choices. Waydroid's hwcomposer sets the Wayland
# app_id of its window to "Waydroid" for the full UI and "waydroid.<package>"
# for a per-app window, and luna-surfacemanager carries that through to
# WebOSSurfaceItem::appId. Naming the webOS apps the same way is what lets the
# card switcher match a card to its app.
#
# Every container property here goes through "waydroid prop", never through
# lxc-attach. Waydroid freezes the container whenever Android suspends - which
# it does as soon as nothing is displayed - and lxc-attach into a frozen
# container blocks forever, so a launcher that asked for sys.boot_completed
# that way hung before it ever launched anything. "waydroid prop" unfreezes
# first, and refreezes afterwards only if it was the one that unfroze.
set -u

. /usr/libexec/waydroid-luneos-session-env

PKG=${1:-}
POLL=5
# Consecutive answered "nothing is shown" polls before the instance is ended.
MAX_MISSES=3
# Bound on a single launch attempt, before it is treated as failed.
LAUNCH_TIMEOUT=45

log() { echo "waydroid-app${PKG:+ $PKG}: $*"; }

# Every "waydroid prop" call is bounded, because it can block indefinitely.
#
# tools/helpers/props.py reads a property through IPlatform.get_service(),
# which waits for the WayDroid platform service inside the container. That
# service does not exist until Android has booted, so before then the call
# never returns - it does not fail, it hangs. A plain retry loop around it
# therefore never gets to iterate: the first call blocks forever, the launcher
# stops before it launches anything, and every press of the icon leaves another
# wedged process behind.
#
# There is no timeout(1) in busybox here, so bound it by hand: run it in the
# background, wait a fixed number of seconds, and kill it if it is still there.
# A timed-out read answers empty, which every caller already treats as unknown.
WPROP_TIMEOUT=20

# Prints the value, and says through its exit status whether that value means
# anything:
#
#   0  the property was read; stdout is what Android answered
#   1  the read did not finish in time; stdout is empty and tells us nothing
#
# The distinction is the whole point. A timed-out read answers empty, and empty
# is also what an unset property answers, so a caller that only looks at stdout
# cannot tell "Android says nothing is shown" from "Android did not answer".
# Treating the second as the first is what used to close apps that were still
# running: one slow poll and the launcher decided the app had gone.
#
# Reads do go slow. Every one of them unfreezes the container, walks to the
# WayDroid platform service and back, and refreezes; while Android is busy
# starting an activity that round trip can take tens of seconds. Measured on
# sargo, a poll that normally answers in under two seconds hit the old ten
# second bound during app launch.
wprop() {
    # The daemon publishes waydroid.active_apps for as long as something is
    # open, and reading that costs nothing at all.
    if [ "$1" = "waydroid.active_apps" ] && [ -f "$LAUNCHD_STATUS" ]; then
        tr -d '\r\n' < "$LAUNCHD_STATUS"
        return 0
    fi

    _out=$(mktemp) || return 1
    /usr/bin/waydroid prop get "$1" >"$_out" 2>/dev/null &
    _pid=$!
    _n=0
    while [ "$_n" -lt "$WPROP_TIMEOUT" ] && kill -0 "$_pid" 2>/dev/null; do
        _n=$((_n + 1))
        sleep 1
    done
    _rc=0
    if kill -0 "$_pid" 2>/dev/null; then
        kill -9 "$_pid" 2>/dev/null
        _rc=1
    fi
    wait "$_pid" 2>/dev/null
    tr -d '\r\n' < "$_out"
    rm -f "$_out"
    return "$_rc"
}

wpropset() { /usr/bin/waydroid prop set "$1" "$2" >/dev/null 2>&1; }

# The warm launcher, when it is running.
#
# Everything below that talks to Android through /usr/bin/waydroid pays for a
# fresh Python process: the CLI imports gbinder, dbus and the tools package
# every time, which on a phone under memory pressure comes back off flash.
# Measured on sargo: 12.97s of imports, against 9-512ms for the launch itself
# once those are done. waydroid-luneos-launchd holds them open, so a launch
# here is a shell redirect into a FIFO and a property read is a file read.
#
# Both paths keep working when it is not running - it is a speed-up, not a
# dependency - so every use falls back to the CLI.
LAUNCHD_FIFO=/run/waydroid-luneos/launch
LAUNCHD_STATUS=/run/waydroid-luneos/active_apps

launchd_up() { [ -p "$LAUNCHD_FIFO" ]; }

launchd_send() {
    launchd_up || return 1
    # Never block on a FIFO with no reader: if the daemon died between the test
    # above and here, this would hang forever.
    ( echo "$*" > "$LAUNCHD_FIFO" ) 2>/dev/null &
    _sp=$!
    _sn=0
    while [ "$_sn" -lt 5 ] && kill -0 "$_sp" 2>/dev/null; do
        _sn=$((_sn + 1))
        sleep 1
    done
    if kill -0 "$_sp" 2>/dev/null; then
        kill -9 "$_sp" 2>/dev/null
        wait "$_sp" 2>/dev/null
        return 1
    fi
    wait "$_sp"
}

# Runs a command under a bound, the same way wprop does and for the same reason
# - there is no timeout(1) in busybox here.
#
#   0      the command finished and succeeded
#   1      it did not finish in time and was killed
#   other  its own exit status
wrun() {
    _limit=$1
    shift
    "$@" >/dev/null 2>&1 &
    _p=$!
    _k=0
    while [ "$_k" -lt "$_limit" ] && kill -0 "$_p" 2>/dev/null; do
        _k=$((_k + 1))
        sleep 1
    done
    if kill -0 "$_p" 2>/dev/null; then
        kill -9 "$_p" 2>/dev/null
        wait "$_p" 2>/dev/null
        return 1
    fi
    wait "$_p"
}

waydroid_luneos_session_env 60 || exit 1

# Initialise on demand, because there is no boot-time unit doing it any more.
#
# "waydroid init" fetches the Android system and vendor images, so it needs a
# network - which a freshly flashed device does not have at boot, and which is
# why running it from a unit produced a failed service and, through the
# ordering, a failed container. Asking for Android is the moment the user has
# both decided they want it and, usually, joined a network. It takes a while:
# on sargo the pair is a little under 2 GB.
if [ ! -f /var/lib/waydroid/waydroid.cfg ]; then
    log "not initialised yet; fetching the Android images, this will take a while"
    # Binder nodes and the image mount have to exist before the container is
    # asked for anything. The unit is Before= the container, but nothing has
    # pulled it in on a device that has never been initialised.
    systemctl start waydroid-luneos-prepare.service >/dev/null 2>&1 || true
    if ! /usr/bin/waydroid init; then
        log "initialisation failed - check the network and try again"
        exit 1
    fi
    log "initialised"
fi

# The session owns the Wayland connection Android composites through, so it has
# to be up before anything can be shown. It normally already is; on a device
# that was just initialised, neither it nor the container has ever run.
systemctl is-active --quiet waydroid-container.service ||
    systemctl start waydroid-container.service || true
systemctl is-active --quiet waydroid-luneos-session.service ||
    systemctl start waydroid-luneos-session.service || true

closing=0
cleaned=0

# Runs on the way out, whichever way that is.
#
# This used to be wired to TERM and INT only, so it did not run when the script
# simply returned - and returning is the normal end, when Android stops showing
# anything. That left waydroid.active_apps still naming the package. hwcomposer
# reads that property as "this app has a window up", so the next launch of the
# same app created no Wayland surface at all: waydroid app launch succeeded,
# SAM handed back an instance id, and nothing ever appeared. The app was stuck
# until something else reset the property.
#
# SIGKILL still cannot be trapped, so a launcher killed outright will leave the
# same stale value behind.
cleanup() {
    if [ -z "$PKG" ]; then
        # waydroid.active_apps=none puts hwcomposer into closed_mode, whose
        # cleanup_stale_windows() calls clear_open_windows() - the same end
        # state as swiping the card away, and the only lever for the full UI.
        wpropset waydroid.active_apps none
    else
        # For a single app, force-stop drops the task, and hwcomposer erases
        # the window with it - measured going from open_windows=1 to 0.
        #
        # It has to go through "waydroid shell", not lxc-attach: am and cmd are
        # app_process wrappers that need ANDROID_ROOT, ANDROID_DATA and the
        # rest, and lxc-attach hands them the host's environment instead, so
        # they abort with status 255 and no output. "waydroid shell" sets up
        # the container environment properly.
        /usr/bin/waydroid shell -- /system/bin/sh -c "am force-stop $PKG" >/dev/null 2>&1
    fi
}

run_cleanup() {
    [ "$cleaned" -eq 1 ] && return 0
    cleaned=1
    [ -n "$PKG" ] && launchd_send "close $PKG"
    cleanup
}

on_term() {
    closing=1
    run_cleanup
}

trap on_term TERM INT
trap run_cleanup EXIT

# Ask for the app straight away, and only work out why if that fails.
#
# There used to be a "wait for sys.boot_completed" loop here, ahead of every
# launch. It was most of the time a launch took. The cost is not Android and it
# is not the container being frozen - it is the question: "waydroid prop get"
# goes through IPlatform.get_service() and waits on the container's platform
# service, where "waydroid app launch" does not. On a container that has been
# idle a while the first prop read costs about eight seconds; a second one
# straight after costs none, so it is a cold-path cost, not a per-read one.
#
# Measured on sargo, launching Gallery from an idle container: fourteen seconds
# to the window through the probe, four seconds without it.
#
# Waiting still has to happen when Android genuinely has not booted - a device
# that just started, or one that was only initialised a moment ago. That is the
# uncommon case, so it belongs after a failure rather than ahead of every
# success.
launch_once() {
    if [ -z "$PKG" ]; then
        # show-full-ui is not idempotent: it ends with a "refresh display
        # contents" step that makes hwcomposer rebuild its window, so asking
        # for a UI that is already up replaces the card rather than raising it.
        # waydroid.active_apps is the right question - "is a window shown" -
        # where session state is not, since the session can be running with
        # nothing on screen.
        if [ "$(wprop waydroid.active_apps)" = "Waydroid" ]; then
            return 0
        fi
        wrun "$LAUNCH_TIMEOUT" /usr/bin/waydroid show-full-ui
    else
        if launchd_send "launch $PKG"; then
            return 0
        fi
        wrun "$LAUNCH_TIMEOUT" /usr/bin/waydroid app launch "$PKG"
    fi
}

if ! launch_once; then
    log "launch did not take; waiting for Android to finish booting"
    i=0
    booted=0
    while [ "$i" -lt 240 ] && [ "$closing" -eq 0 ]; do
        if [ "$(wprop sys.boot_completed)" = "1" ]; then
            booted=1
            break
        fi
        # wprop already spent up to WPROP_TIMEOUT seconds when Android is not up.
        i=$((i + WPROP_TIMEOUT + 2))
        sleep 2
    done
    [ "$booted" -eq 1 ] || { log "Android did not finish booting"; exit 1; }
    launch_once || { log "launch failed"; exit 1; }
fi

# Give Android a moment to put something up before watching for it to go away,
# or this exits immediately and SAM never sees the app run at all.
i=0
shown=0
while [ "$i" -lt 60 ] && [ "$closing" -eq 0 ]; do
    a=$(wprop waydroid.active_apps) || a=""
    if [ -n "$a" ] && [ "$a" != "none" ]; then
        shown=1
        break
    fi
    i=$((i + 2))
    sleep 2
done

# Held open while Android is showing anything, rather than while this
# particular app is the one in front. In single-window mode only one Android
# app is displayed at a time and the others are kept as snapshot cards, so
# "this app is frontmost" would end the instance every time the user switched
# apps, and SAM would call an app closed while its card was still there. The
# cost is the opposite: an app stays listed as running until Android as a whole
# is closed.
if [ "$shown" -eq 1 ]; then
    log "on screen; holding the SAM instance open"
else
    # Held open anyway. Not having seen it yet is not evidence it failed, and
    # exiting here would have SAM close an app that is still on its way up.
    log "not up yet; holding the SAM instance open anyway"
fi

# Only an answered "nothing is shown" ends the instance. A read that did not
# come back says nothing about the app, and ending on one is what closed cards
# out from under the user: the script exits, SAM sees its process go, and the
# card is torn down while Android still has the app on screen.
#
# Even a real answer is only acted on after MAX_MISSES of them in a row, since
# waydroid.active_apps reads empty for a moment while Android hands over
# between activities.
misses=0
while [ "$closing" -eq 0 ]; do
    sleep "$POLL"
    [ "$closing" -eq 0 ] || break

    if a=$(wprop waydroid.active_apps); then
        if [ "$a" = "none" ] || [ -z "$a" ]; then
            misses=$((misses + 1))
            if [ "$misses" -ge "$MAX_MISSES" ]; then
                log "nothing shown any more"
                break
            fi
        else
            misses=0
        fi
    else
        log "container did not answer; still holding"
    fi
done
exit 0
