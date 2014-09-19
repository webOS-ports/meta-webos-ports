SUMMARY = "HW accelerated colorspace converter"
SECTION = "multimedia"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "gstreamer gst-plugins-base gst-jolla virtual/libhardware"

PV = "1.0.0+gitr${SRCPV}"

SRC_URI = "git://github.com/sailfishos/gst-colorconv.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

SRCREV = "16fbaa7770f36d94163a9cac62b7bcbd7f9dda91"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/gstreamer-0.10/*.so ${libdir}/gstcolorconv/*.so.*"
FILES_${PN}-dbg += "${libdir}/gstreamer-0.10/.debug ${libdir}/gstcolorconv/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-0.10/*.la ${libdir}/gstcolorconv/*.la ${libdir}/gstcolorconv/*.so"
FILES_${PN}-staticdev += "${libdir}/gstreamer-0.10/*.a"
