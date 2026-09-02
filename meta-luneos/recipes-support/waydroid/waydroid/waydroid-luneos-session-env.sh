# Resolve the LuneOS session environment. Sourced, not executed.
#
# Waydroid expects to be started from inside a user session that already has
# XDG_RUNTIME_DIR, WAYLAND_DISPLAY and DBUS_SESSION_BUS_ADDRESS set:
# session_manager.py reads XDG_RUNTIME_DIR to find the Wayland socket, and
# tools/helpers/ipc.py talks to id.waydro.Session over dbus.SessionBus().
#
# LuneOS has no login session for Waydroid to attach to. The compositor runs as
# root with its own session bus and XDG_RUNTIME_DIR=/tmp/xdg, which is neither
# the systemd default nor the /run/luna-session the upstream units set up. So
# rather than assume a session, read the one that exists off the compositor.
#
# On success sets and exports XDG_RUNTIME_DIR, WAYLAND_DISPLAY,
# DBUS_SESSION_BUS_ADDRESS and XDG_SESSION_TYPE, and returns 0.

WAYDROID_COMPOSITOR=${WAYDROID_COMPOSITOR:-surface-manager}

waydroid_luneos_session_env() {
    timeout=${1:-120}
    _pid=""

    # The process exists well before it is serving, so wait for the socket too.
    _i=0
    while [ "$_i" -lt "$timeout" ]; do
        _pid=$(pgrep -f "$WAYDROID_COMPOSITOR" | head -n 1)
        [ -n "$_pid" ] && break
        _i=$((_i + 2))
        sleep 2
    done
    [ -n "$_pid" ] || { echo "no $WAYDROID_COMPOSITOR after ${timeout}s" >&2; return 1; }

    _env_of() {
        tr '\0' '\n' < "/proc/$_pid/environ" 2>/dev/null | grep "^$1=" | cut -d= -f2-
    }

    XDG_RUNTIME_DIR=$(_env_of XDG_RUNTIME_DIR)
    DBUS_SESSION_BUS_ADDRESS=$(_env_of DBUS_SESSION_BUS_ADDRESS)
    WAYLAND_DISPLAY=$(_env_of WAYLAND_DISPLAY)
    [ -n "$XDG_RUNTIME_DIR" ] || { echo "compositor has no XDG_RUNTIME_DIR" >&2; return 1; }
    [ -n "$WAYLAND_DISPLAY" ] || WAYLAND_DISPLAY=wayland-0
    XDG_SESSION_TYPE=wayland
    export XDG_RUNTIME_DIR DBUS_SESSION_BUS_ADDRESS WAYLAND_DISPLAY XDG_SESSION_TYPE

    # Android's audio goes to the host PulseAudio, which Waydroid bind mounts
    # into the container as $PULSE_RUNTIME_PATH/native. Left unset, Waydroid
    # derives that from XDG_RUNTIME_DIR, which is where a per-user PulseAudio
    # puts its socket - LuneOS runs one system-wide instance instead
    # ("pulseaudio --system"), whose socket is /run/pulse/native, so the derived
    # path does not exist and the container gets no audio device at all.
    if [ -z "${PULSE_RUNTIME_PATH:-}" ] && [ ! -S "$XDG_RUNTIME_DIR/pulse/native" ]; then
        for _p in /run/pulse /var/run/pulse; do
            if [ -S "$_p/native" ]; then
                PULSE_RUNTIME_PATH=$_p
                export PULSE_RUNTIME_PATH
                break
            fi
        done
    fi

    _i=0
    while [ "$_i" -lt "$timeout" ]; do
        [ -S "$XDG_RUNTIME_DIR/$WAYLAND_DISPLAY" ] && return 0
        _i=$((_i + 2))
        sleep 2
    done
    echo "wayland socket $XDG_RUNTIME_DIR/$WAYLAND_DISPLAY never appeared" >&2
    return 1
}
