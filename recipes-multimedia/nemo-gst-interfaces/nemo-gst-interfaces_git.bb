SUMMARY = "Nemo mobile specific GStreamer interfaces"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += "gstreamer"

PV = "1.0.0+gitr${SRCPV}"
PR = "r0"

SRC_URI = "git://github.com/nemomobile/nemo-gst-interfaces.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

SRCREV = "e36a25942c7ef7c41955d5ccc35862f6b1c16c06"

inherit autotools
