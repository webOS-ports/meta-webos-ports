#!/bin/sh
# Recover the vendor camera provider when it comes up wedged, before anything
# tries to use a camera.
#
# On sargo (Pixel 3a, Android 12 vendor under an Android 16 GSI) the vendor
# camera provider that init starts from "on late-fs" comes up unusable on
# roughly half of all boots. Nothing about the boot looks different when it
# does: the kernel probes both sensors successfully either way and logs
#
#   CAM-SENSOR: cam_sensor_driver_cmd: Probe Succees,slot:0,...,sensor_id:0x363
#   CAM-SENSOR: cam_sensor_driver_cmd: Probe Succees,slot:1,...,sensor_id:0x355
#
# What fails is the first configure_streams. CamX cannot acquire the sensor:
#
#   [CSL   ] CSLHwInternalDefaultIoctl() Ioctl failed for device /dev/v4l-subdevN
#            (Type:CSLHwImageSensor) with error reason Invalid argument
#   [SENSOR] AcquireDevice() AcquireDevice on Sensor failed
#   [SENSOR] OnStreamOn() Creating submodules failed, cannot setup sensor
#   [CHI   ] CreateSession() Unable to create session
#   [HAL   ] ConfigureStreams() CHI Module did not override configure streams
#
# with the kernel refusing the CAM_ACQUIRE_DEV ioctl underneath -
# "CAM-SENSOR: cam_sensor_driver_cmd: Not in right state to aquire". Camera3Device
# reports -38 up to the API1 shim, Camera2Client turns that into -22, and
# gst-droid finally surfaces it to its callers as "error 0x1 from camera HAL".
# The visible result is a black viewfinder in every camera app, with
# com.webos.service.camera2 answering startCamera with errorCode 35.
#
# The kernel side is the whole story. cam_sensor_core.c refuses CAM_ACQUIRE_DEV
# unless both is_probe_succeed is set and sensor_state is CAM_SENSOR_INIT, and
# the warning prints sensor_state - which is 0, i.e. INIT - so the flag that is
# clear is is_probe_succeed. The only code that clears it is cam_sensor_shutdown(),
# and cam_sensor_dev.c wires that up as the subdev's .close handler:
#
#   static int cam_sensor_subdev_close(...)  { ... cam_sensor_shutdown(s_ctrl); }
#   void cam_sensor_shutdown(...)            { ... s_ctrl->is_probe_succeed = 0; }
#
# So *closing* a file handle on the sensor's /dev/v4l-subdevN invalidates the
# probe result, and from then on every acquire is refused until something probes
# the sensor again. Confirmed directly: on a device with a working camera,
# opening and closing the v4l-subdev nodes and nothing else breaks it instantly,
# with one new "Not in right state to aquire" in dmesg.
#
# Nothing on the LuneOS side does that - udev is already kept away from these
# nodes by the comment-only /etc/udev/rules.d/60-persistent-v4l.rules override,
# halium-generate-udev-rules deliberately does not re-fire add events, and with
# com.webos.service.camera masked the failure still happens. What remains is
# CamX's own transient handles during its two-sensor probe, which also fits the
# roughly one-in-two hit rate. Google shipped no fix: android-msm-bonito-4.9-android12L
# is the last branch for this device and still clears the flag on close.
#
# The one thing that reliably restores it is a re-probe, which is what the
# provider does on startup - hence restart. Probe rather than restart blindly,
# because a restart from start-android-hals.sh at about nineteen seconds into
# boot was measured NOT to help (the replacement came up wedged too) while a
# later one does; checking for an actual frame avoids encoding a magic delay.
# On a healthy boot this costs one camera open, a little over a second, and no
# restart at all.
#
# The probe deliberately opens the camera through droidcamsrc rather than
# talking to the HAL directly: that is the exact path every real client takes
# (gst-droid -> droidmedia -> Camera2Client -> CamX), so it fails when and only
# when they would.

PLUGINDIR="@GST_DROID_PLUGINDIR@"
ATTEMPTS=4
CAMERA_DEVICE=0

# gst-droid-gate.service normally puts the plugin dir on GST_PLUGIN_PATH for
# every unit started after it, but do not depend on having inherited it.
[ -e "${PLUGINDIR}/libgstdroid.so" ] || {
    echo "gst-droid is not present, nothing to heal"
    exit 0
}
case ":${GST_PLUGIN_PATH}:" in
    *":${PLUGINDIR}:"*) ;;
    ::)                 GST_PLUGIN_PATH="${PLUGINDIR}" ;;
    *)                  GST_PLUGIN_PATH="${GST_PLUGIN_PATH}:${PLUGINDIR}" ;;
esac
export GST_PLUGIN_PATH

command -v gst-launch-1.0 >/dev/null 2>&1 || {
    echo "gst-launch-1.0 not available, cannot probe the camera"
    exit 0
}
command -v setprop >/dev/null 2>&1 || {
    echo "setprop not available, cannot restart the camera provider"
    exit 0
}

# gst-launch's exit status is not a reliable signal here - it has been seen to
# return 0 for a pipeline that never produced anything - so key off the frame
# gst-droid announces when the buffer queue first delivers ("CAMERA_STARTUP
# ... first buffer-queue frame available"). No frame, no working camera.
probe_camera() {
    gst-launch-1.0 -q droidcamsrc camera-device="${CAMERA_DEVICE}" \
        ! fakesink num-buffers=1 2>&1 | grep -q "first buffer"
}

# Match on the binary path rather than a service name: sargo declares
# android.hardware.camera.provider@2.4-service_64, other boards -service,
# -external, or an AIDL provider under a different name entirely.
cam_svcs=$(
    for d in /android/vendor/etc/init /android/odm/etc/init; do
        [ -d "$d" ] || continue
        for f in "$d"/*.rc "$d"/hw/*.rc; do
            [ -f "$f" ] || continue
            awk '/^service / && $3 ~ /camera\.provider/ { print $2 }' "$f"
        done
    done 2>/dev/null | sort -u
)

restart_providers() {
    [ -n "$cam_svcs" ] || {
        echo "no camera provider service found in the container's rc files"
        return 1
    }
    for s in $cam_svcs; do
        [ "$(getprop "init.svc.$s")" = "running" ] || continue
        echo "restarting $s"
        setprop ctl.stop "$s"
        i=0
        while [ $i -lt 40 ]; do
            [ "$(getprop "init.svc.$s")" = "running" ] || break
            i=$((i + 1))
            sleep 0.25
        done
        setprop ctl.start "$s"
        i=0
        while [ $i -lt 60 ]; do
            [ "$(getprop "init.svc.$s")" = "running" ] && break
            i=$((i + 1))
            sleep 0.25
        done
    done
    # The service being "running" only means init forked it; CamX still has to
    # re-probe the sensors before it can answer an open.
    sleep 4
    return 0
}

attempt=1
while [ $attempt -le $ATTEMPTS ]; do
    if probe_camera; then
        if [ $attempt -eq 1 ]; then
            echo "camera HAL answered on the first probe, nothing to do"
        else
            echo "camera HAL answered after $((attempt - 1)) provider restart(s)"
        fi
        exit 0
    fi

    echo "camera probe $attempt/$ATTEMPTS produced no frame"
    [ $attempt -eq $ATTEMPTS ] && break
    restart_providers || break
    attempt=$((attempt + 1))
done

# Never fail the unit: a camera that does not work is not a reason to hold up
# or degrade the rest of the boot.
echo "WARNING: camera HAL still produced no frame after $ATTEMPTS probes"
exit 0
