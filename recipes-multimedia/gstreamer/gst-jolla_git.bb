SUMMARY = "Collection of Jolla specific GStreamer plugins"
SECTION = "multimedia"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = " \
    gstreamer gst-plugins-base \
    virtual/libhardware \
    virtual/android-headers \
    virtual/egl \
    virtual/libgles2 \
    nemo-gst-interfaces \
"

PV = "1.0.0+gitr${SRCPV}"

SRC_URI = "git://github.com/sailfishos/gst-jolla.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

SRCREV = "c93b5aba2bf6203ee8887bc15c5007affde1201b"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/gstreamer-0.10/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-0.10/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-0.10/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-0.10/*.a"
