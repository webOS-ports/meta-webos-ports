SUMMARY = "GStreamer OpenMAX IL wrappers"
SECTION = "multimedia"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = " \
    gstreamer gst-plugins-base gst-jolla \
    virtual/libhardware \
"

PV = "1.0.0+gitr${SRCPV}"

SRC_URI = " \
    git://github.com/sailfishos/gst-omx.git;branch=master;protocol=git;name=omx \
    git://anongit.freedesktop.org/git/gstreamer/common.git;name=common;branch=master;protocol=http;destsuffix=git/common \
"
S = "${WORKDIR}/git"

SRCREV_omx = "d69c7207ca26c0db087b2ec2fcf2c2ecd2f965c8"
SRCREV_common = "116ba9b1446a420d0062b3a0d6178b424b6f8645"

SRCREV_FORMAT = "omx"

inherit autotools pkgconfig

EXTRA_OECONF += " \
    --disable-statis \
    --enable-hybris=yes \
"

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

do_install_append() {
    install -d ${D}${sysconfdir}/xdg
    install -m 0544 ${S}/omx/gstomx.conf ${D}${sysconfdir}/xdg
}

FILES_${PN} += "${libdir}/gstreamer-0.10/*.so ${sysconfdir}/xdg"
FILES_${PN}-dbg += "${libdir}/gstreamer-0.10/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-0.10/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-0.10/*.a"
