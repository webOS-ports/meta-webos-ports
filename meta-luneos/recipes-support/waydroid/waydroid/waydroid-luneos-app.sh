#!/bin/sh
# Launch an Android application from a webOS launcher tile and stay in the
# foreground for as long as it is on screen.
#
#   waydroid-luneos-app             show the Android full UI (app id Waydroid)
#   waydroid-luneos-app <package>   show one app     (app id waydroid.<package>)
#
# Being long-running is the point, not an accident. SAM tracks a native app by
# the process it spawned, so the original one-line launcher - which asked
# Waydroid to start something and exited - was never "running": the launcher
# never marked the app open and com.webos.applicationManager/close answered
# "is not running" while the card sat there. Staying alive also gives closing
# somewhere to land, because SAM closes a native app by signalling its process.
#
# The app ids are not free choices. Waydroid's hwcomposer sets the Wayland
# app_id of its window to "Waydroid" for the full UI and "waydroid.<package>"
# for a per-app window, and luna-surfacemanager carries that through to
# WebOSSurfaceItem::appId. Naming the webOS apps the same way is what lets the
# card switcher match a card to its app.
set -u

. /usr/libexec/waydroid-luneos-session-env

PKG=${1:-}
LXC=/var/lib/waydroid/lxc
POLL=5

log() { echo "waydroid-app${PKG:+ $PKG}: $*"; }

in_container() { lxc-attach -P "$LXC" -n waydroid -- "$@" 2>/dev/null; }
getprop() { in_container /system/bin/getprop "$1"; }

waydroid_luneos_session_env 60 || exit 1

# The session owns the Wayland connection Android composites through, so it has
# to be up before anything can be shown. It normally already is.
systemctl is-active --quiet waydroid-luneos-session.service ||
    systemctl start waydroid-luneos-session.service || true

i=0
while [ "$i" -lt 240 ]; do
    [ "$(getprop sys.boot_completed)" = "1" ] && break
    i=$((i + 3))
    sleep 3
done
[ "$(getprop sys.boot_completed)" = "1" ] || { log "Android did not finish booting"; exit 1; }

closing=0
on_term() {
    closing=1
    if [ -z "$PKG" ]; then
        # waydroid.active_apps=none puts hwcomposer into closed_mode, whose
        # cleanup_stale_windows() calls clear_open_windows() - the same end
        # state as swiping the card away, and the only lever the host has.
        in_container /system/bin/setprop waydroid.active_apps none
    fi
    # For a single app there is no such lever, so this deliberately leaves the
    # container alone. hwcomposer only drops one task's window through
    # xdg_toplevel_handle_close(), which inserts the task into ignored_apps and
    # erases the window - and that is reachable only from the compositor, which
    # is what swiping the card away does. Nothing on the host substitutes for
    # it: "am"/"cmd" abort inside the container (they are app_process wrappers
    # and lxc-attach gives them the host environment, with no ANDROID_* in it),
    # and killing the app's process leaves the task, so the card would survive
    # as a snapshot of a dead app - worse than leaving it alone. So SAM's close
    # ends this instance and the card stays until it is swiped.
}
trap on_term TERM INT

if [ -z "$PKG" ]; then
    # show-full-ui is not idempotent: it ends with a "refresh display contents"
    # step that makes hwcomposer rebuild its window, so asking for a UI that is
    # already up replaces the card rather than raising it. waydroid.active_apps
    # is the right question - "is a window shown" - where session state is not,
    # since the session can be running with nothing on screen.
    if [ "$(getprop waydroid.active_apps)" != "Waydroid" ]; then
        /usr/bin/waydroid show-full-ui || { log "show-full-ui failed"; exit 1; }
    fi
else
    /usr/bin/waydroid app launch "$PKG" || { log "app launch failed"; exit 1; }
fi

# Give Android a moment to actually put something up before watching for it to
# go away, or this exits immediately and SAM never sees the app run at all.
i=0
while [ "$i" -lt 60 ] && [ "$closing" -eq 0 ]; do
    if [ -z "$PKG" ]; then
        [ "$(getprop waydroid.active_apps)" = "Waydroid" ] && break
    else
        [ -n "$(in_container /system/bin/pidof "$PKG")" ] && break
    fi
    i=$((i + 2))
    sleep 2
done

log "on screen; holding the SAM instance open"
while [ "$closing" -eq 0 ]; do
    sleep "$POLL"
    [ "$closing" -eq 0 ] || break
    if [ -z "$PKG" ]; then
        a=$(getprop waydroid.active_apps)
        if [ "$a" = "none" ] || [ -z "$a" ]; then
            log "window gone"
            break
        fi
    elif [ -z "$(in_container /system/bin/pidof "$PKG")" ]; then
        log "app process gone"
        break
    fi
done
exit 0
