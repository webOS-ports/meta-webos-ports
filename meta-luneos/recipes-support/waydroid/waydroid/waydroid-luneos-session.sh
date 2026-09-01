#!/bin/sh
# Start a Waydroid session against the LuneOS compositor.
#
# INTERIM. Waydroid splits work between a root container service and a per-user
# session, and expects the session to be started from inside a real user session
# that already has XDG_RUNTIME_DIR, WAYLAND_DISPLAY and DBUS_SESSION_BUS_ADDRESS
# set. LuneOS has no such session for Waydroid to attach to, so this reads them
# back off the running compositor instead.
#
# That is a workaround, not a design. The proper fix is to start
# "waydroid session start" from the LuneOS session as the session user and keep
# only "waydroid container start" as a root service; this exists so a device
# comes up with Android ready in the meantime.
set -u

log() { echo "waydroid-session: $*"; }

# Wait for the compositor. Its socket appearing is the signal, not the process:
# the process exists well before it is serving.
COMPOSITOR=surface-manager
for _ in $(seq 1 60); do
    pid=$(pgrep -f "$COMPOSITOR" | head -n 1)
    [ -n "${pid:-}" ] && break
    sleep 2
done
[ -n "${pid:-}" ] || { log "no $COMPOSITOR after 120s, giving up"; exit 1; }

# Take the session's own values rather than assuming them: LuneOS puts
# XDG_RUNTIME_DIR at /tmp/xdg, which is neither the systemd default nor the
# /run/luna-session the waydroid units set up.
env_of() { tr '\0' '\n' < "/proc/$pid/environ" 2>/dev/null | grep "^$1=" | cut -d= -f2-; }

XDG_RUNTIME_DIR=$(env_of XDG_RUNTIME_DIR)
DBUS_SESSION_BUS_ADDRESS=$(env_of DBUS_SESSION_BUS_ADDRESS)
WAYLAND_DISPLAY=$(env_of WAYLAND_DISPLAY)
[ -n "$XDG_RUNTIME_DIR" ] || { log "compositor has no XDG_RUNTIME_DIR"; exit 1; }
[ -n "$WAYLAND_DISPLAY" ] || WAYLAND_DISPLAY=wayland-0
export XDG_RUNTIME_DIR DBUS_SESSION_BUS_ADDRESS WAYLAND_DISPLAY
export XDG_SESSION_TYPE=wayland

for _ in $(seq 1 60); do
    [ -S "$XDG_RUNTIME_DIR/$WAYLAND_DISPLAY" ] && break
    sleep 2
done
[ -S "$XDG_RUNTIME_DIR/$WAYLAND_DISPLAY" ] || {
    log "wayland socket $XDG_RUNTIME_DIR/$WAYLAND_DISPLAY never appeared"; exit 1
}

log "starting session on $XDG_RUNTIME_DIR/$WAYLAND_DISPLAY"
exec /usr/bin/waydroid session start
