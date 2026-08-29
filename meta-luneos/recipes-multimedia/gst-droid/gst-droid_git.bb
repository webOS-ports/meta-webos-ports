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
"

inherit meson pkgconfig

FILES:${PN} += "${libdir}/gstreamer-1.0/libgstdroid.so"
