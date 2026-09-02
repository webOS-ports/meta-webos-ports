#!/bin/sh
# Start a Waydroid session against the LuneOS compositor.
#
# Waydroid splits its work between a root container service and a per-user
# session. LuneOS has no login session to start the second half from - the
# compositor runs as root with its own session bus - so the session environment
# is read back off the compositor instead of assumed. See
# waydroid-luneos-session-env for why that is the honest model here rather than
# a workaround: on LuneOS root *is* the session user, and the alternative was
# a faked XDG_RUNTIME_DIR that disagreed with what session_manager.py derives.
set -u

. /usr/libexec/waydroid-luneos-session-env

log() { echo "waydroid-session: $*"; }

waydroid_luneos_session_env 120 || exit 1

log "starting session on $XDG_RUNTIME_DIR/$WAYLAND_DISPLAY"
exec /usr/bin/waydroid session start
