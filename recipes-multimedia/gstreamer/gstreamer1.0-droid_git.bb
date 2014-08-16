SUMMARY = "Android specific GStreamer plugin"
HOMEPAGE = "https://gitorious.org/gstreamer1-0/gst-droid"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

PV = "1.0.0+gitr${SRCPV}"
PR = "r0"

DEPENDS = "gstreamer1.0 nemo-gst-interfaces libexif"

SRC_URI = "git://gitorious.org/gstreamer1-0/gst-droid.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

SRCREV = "009bc7de9d84458d00b4009aaaaff33c0df7992d"

inherit autotools

FILES_${PN} += "${datadir}/gstreamer-1.0 ${libdir}/gstreamer-1.0/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-1.0/*.a"
