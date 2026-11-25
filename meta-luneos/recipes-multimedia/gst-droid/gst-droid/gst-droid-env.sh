#!/bin/sh
# gst-droid-env <command> [args...]: run a command with the GStreamer environment
# gst-droid-gate.sh chose, then exec it.
#
# The gate puts that environment on the systemd manager, which only reaches
# units systemd starts after it. A bus service that ls-hubd launches on demand
# inherits ls-hubd's environment instead, set before the gate ran, so it never
# saw libgstdroid.so: luneos-rtc-engine found no droidcamsrc and reported no
# camera. Such a service's Exec= runs it through this. Before the gate has run,
# or where it left gst-droid out, there is no file and the command runs
# unchanged.

ENVFILE="@GST_DROID_ENVFILE@"

if [ -r "${ENVFILE}" ]; then
    set -a
    . "${ENVFILE}"
    set +a
fi

exec "$@"
