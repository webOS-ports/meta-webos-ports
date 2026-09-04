SUMMARY = "GStreamer elements for Android cameras and codecs via droidmedia"
DESCRIPTION = "droidcamsrc, droiddec/droidenc and droideglsink from \
SailfishOS. Talks to the Android-side droidmedia services (camera_service, \
minimediaservice) in the Halium container through the droidmedia hybris \
glue, giving GStreamer - and thus Qt 6 Multimedia's gstreamer backend - \
access to the vendor camera HAL and hardware codecs. Optional per-device \
tuning is read from ${sysconfdir}/gst-droid/gstdroidcamsrcquirks.conf and \
gstdroidcodec.conf (ship those via luneos-device-config)."
HOMEPAGE = "https://github.com/sailfishos/gst-droid"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = " \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-bad \
    nemo-gst-interfaces \
    droidmedia \
    libexif \
    orc \
    virtual/egl \
"
# libdroidmedia.a is linked in statically and dlopens the libhybris linker at
# runtime to reach the Android-side libdroidmedia.so.
RDEPENDS:${PN} += "libhybris"

# Depends on droidmedia/libhybris which have this restriction.
COMPATIBLE_MACHINE = "^halium$"
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0.20260508+git"
SRCREV = "e5152aee90dbb9b81c6bc45073b1fa86fc0c4194"
SRC_URI = "git://github.com/sailfishos/gst-droid.git;branch=master;protocol=https \
    file://0001-droidcamsrc-allow-recorder-video-in-raw-preview.patch \
    file://0002-droidcamsrc-runtime-idr-and-bitrate.patch \
    file://0003-droidcamsrc-set-recording-hint-in-video-mode.patch \
    file://gst-droid-gate.sh \
    file://gst-droid-gate.service \
"

inherit meson pkgconfig systemd

# libgstdroid must not sit in the directory GStreamer scans by default.
#
# It dlopens the container's libdroidmedia.so, which reaches libstagefright's
# MediaCodecList and asks hwservicemanager for
# android.hardware.media.omx@1.0::IOmxStore. libhidl's getService() retries
# forever, so on a Halium base that registers no such service - tissot-halium's
# halium-luneos-9.0 image ships no OMX service at all, and its minimediaservice
# has no OMX support - gst-plugin-scanner hangs and takes every GStreamer client
# with it, surface-manager included: it never reaches sd_notify(READY=1), its
# Type=notify start times out, and the device sits in a compositor restart loop
# showing a black screen.
#
# Devices whose Android side does provide the service get the plugin as before,
# via gst-droid-gate.service. The cost of guessing wrong in this direction is
# only the loss of hardware codecs; guessing wrong in the other direction costs
# the whole UI, so the plugin stays gated rather than gating on a machine list.
GST_DROID_PLUGINDIR = "${libdir}/gstreamer-1.0-gated"

do_install:append() {
    install -d ${D}${GST_DROID_PLUGINDIR}
    mv ${D}${libdir}/gstreamer-1.0/libgstdroid.so ${D}${GST_DROID_PLUGINDIR}/
    rmdir ${D}${libdir}/gstreamer-1.0 2>/dev/null || true

    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/gst-droid-gate.sh ${D}${bindir}/
    sed -i -e "s|@GST_DROID_PLUGINDIR@|${GST_DROID_PLUGINDIR}|" \
        ${D}${bindir}/gst-droid-gate.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/gst-droid-gate.service ${D}${systemd_system_unitdir}/
    sed -i -e "s|@BINDIR@|${bindir}|" \
        ${D}${systemd_system_unitdir}/gst-droid-gate.service
}

# binder-ping, used by the gate to ask hwservicemanager whether the OMX service
# is registered, comes from libgbinder-tools.
RDEPENDS:${PN} += "libgbinder-tools"

SYSTEMD_SERVICE:${PN} = "gst-droid-gate.service"

FILES:${PN} += "${GST_DROID_PLUGINDIR}/libgstdroid.so"
