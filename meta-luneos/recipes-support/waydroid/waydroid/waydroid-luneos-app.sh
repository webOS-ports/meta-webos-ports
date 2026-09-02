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

log() { echo "waydroid-app${PKG:+ $PKG}: $*"; }

wprop()    { /usr/bin/waydroid prop get "$1" 2>/dev/null | tr -d '\r\n'; }
wpropset() { /usr/bin/waydroid prop set "$1" "$2" >/dev/null 2>&1; }

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

# "waydroid prop get" prints nothing while the session is still coming up, so
# waiting for the answer covers waiting for the session too.
i=0
while [ "$i" -lt 240 ]; do
    [ "$(wprop sys.boot_completed)" = "1" ] && break
    i=$((i + 3))
    sleep 3
done
[ "$(wprop sys.boot_completed)" = "1" ] || { log "Android did not finish booting"; exit 1; }

closing=0
on_term() {
    closing=1
    if [ -z "$PKG" ]; then
        # waydroid.active_apps=none puts hwcomposer into closed_mode, whose
        # cleanup_stale_windows() calls clear_open_windows() - the same end
        # state as swiping the card away, and the only lever the host has.
        wpropset waydroid.active_apps none
    fi
    # For a single app there is no such lever, so this deliberately leaves the
    # container alone. hwcomposer only drops one task's window through
    # xdg_toplevel_handle_close(), which inserts the task into ignored_apps and
    # erases the window - reachable only from the compositor, which is what
    # swiping the card away does. Nothing on the host substitutes for it:
    # "am"/"cmd" abort inside the container (they are app_process wrappers and
    # lxc-attach hands them the host environment, with no ANDROID_* in it), and
    # killing the app's process leaves the task, so the card would survive as a
    # snapshot of a dead app - worse than leaving it alone. So SAM's close ends
    # this instance and the card stays until it is swiped.
}
trap on_term TERM INT

if [ -z "$PKG" ]; then
    # show-full-ui is not idempotent: it ends with a "refresh display contents"
    # step that makes hwcomposer rebuild its window, so asking for a UI that is
    # already up replaces the card rather than raising it. waydroid.active_apps
    # is the right question - "is a window shown" - where session state is not,
    # since the session can be running with nothing on screen.
    if [ "$(wprop waydroid.active_apps)" != "Waydroid" ]; then
        /usr/bin/waydroid show-full-ui || { log "show-full-ui failed"; exit 1; }
    fi
else
    /usr/bin/waydroid app launch "$PKG" || { log "app launch failed"; exit 1; }
fi

# Give Android a moment to put something up before watching for it to go away,
# or this exits immediately and SAM never sees the app run at all.
i=0
while [ "$i" -lt 60 ] && [ "$closing" -eq 0 ]; do
    a=$(wprop waydroid.active_apps)
    if [ -n "$a" ] && [ "$a" != "none" ]; then
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
log "on screen; holding the SAM instance open"
while [ "$closing" -eq 0 ]; do
    sleep "$POLL"
    [ "$closing" -eq 0 ] || break
    a=$(wprop waydroid.active_apps)
    if [ "$a" = "none" ] || [ -z "$a" ]; then
        log "nothing shown any more"
        break
    fi
done
exit 0
