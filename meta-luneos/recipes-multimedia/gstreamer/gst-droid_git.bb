SUMMARY = "Android specific GStreamer plugin"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad \
    libhybris virtual/android-headers \
    nemo-gst-interfaces libexif droidmedia"

PV = "1.0.0+gitr${SRCPV}"

SRC_URI = " \
    git://github.com/sailfishos/gst-droid.git;branch=master;protocol=git;name=gst-droid \
    git://anongit.freedesktop.org/gstreamer/common;protocol=git;branch=master;destsuffix=git/common;name=gst-common \
"
S = "${WORKDIR}/git"

SRCREV_gst-droid = "6e75c80af0d3e867a05157a54a8d09fd08195a22"
SRCREV_gst-common = "bcb1518c08c889dd7eda06936fc26cad85fac755"

SRCREV_FORMAT = "gst-droid"

inherit autotools-brokensep pkgconfig

FILES_${PN} += "${datadir}/gstreamer-1.0 ${libdir}/gstreamer-1.0/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-1.0/*.a"
