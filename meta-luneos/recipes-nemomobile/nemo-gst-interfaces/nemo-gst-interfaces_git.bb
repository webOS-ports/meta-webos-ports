SUMMARY = "Nemo mobile specific GStreamer interfaces"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += "gstreamer1.0"

PV = "1.0.0+gitr${SRCPV}"

SRC_URI = "git://github.com/nemomobile/nemo-gst-interfaces.git;branch=gstreamer1;protocol=git"
S = "${WORKDIR}/git"

SRCREV = "fc98c41276146e4fd15383fc51faf959b4b101c5"

inherit autotools pkgconfig
