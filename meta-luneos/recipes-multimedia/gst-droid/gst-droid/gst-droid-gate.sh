#!/bin/sh
# Put libgstdroid.so on the GStreamer plugin path, but only once the Android
# media services it needs are actually registered.
#
# libgstdroid dlopens the container's libdroidmedia.so, which reaches
# libstagefright's MediaCodecList and from there asks hwservicemanager for
# android.hardware.media.omx@1.0::IOmxStore. libhidl's getService() retries
# without a limit ("Waited one second for ... Waiting another..."), so on a
# Halium base that registers no such service the call never returns:
# gst-plugin-scanner hangs, the GStreamer registry is never written, and every
# GStreamer client blocks on the scan for as long as it lives. That includes
# surface-manager, which then never reaches sd_notify(READY=1) and is killed by
# its 90s Type=notify start timeout - a restart loop and a black screen, with
# nothing in its own log to say why.
#
# So the plugin ships outside the directory GStreamer scans by default and is
# only exposed here, after the container is up and the service answers.
#
# The environment is set on the systemd manager, so it reaches units started
# after this one - hence the ordering in gst-droid-gate.service. Anything
# already running keeps the environment it was started with.

PLUGINDIR="@GST_DROID_PLUGINDIR@"
SERVICE="android.hardware.media.omx@1.0::IOmxStore/default"

if [ ! -e "${PLUGINDIR}/libgstdroid.so" ]; then
    echo "gst-droid-gate: ${PLUGINDIR}/libgstdroid.so is missing, nothing to enable"
    exit 0
fi

if binder-ping -d /dev/hwbinder "${SERVICE}" >/dev/null 2>&1; then
    # Preserve an existing GST_PLUGIN_PATH rather than clobbering it, and do
    # not append twice if this unit is restarted: the value set below is
    # inherited by everything systemd starts afterwards, this script included.
    case ":${GST_PLUGIN_PATH}:" in
        *":${PLUGINDIR}:"*) NEW="${GST_PLUGIN_PATH}" ;;
        ::)                 NEW="${PLUGINDIR}" ;;
        *)                  NEW="${GST_PLUGIN_PATH}:${PLUGINDIR}" ;;
    esac
    systemctl set-environment "GST_PLUGIN_PATH=${NEW}"
    echo "gst-droid-gate: ${SERVICE} is registered, added ${PLUGINDIR} to GST_PLUGIN_PATH"
else
    echo "gst-droid-gate: ${SERVICE} is not registered; leaving gst-droid out of the"
    echo "gst-droid-gate: plugin path. Hardware codecs and droidcamsrc are unavailable,"
    echo "gst-droid-gate: but GStreamer clients will not hang scanning for them."
fi

exit 0
