SUMMARY = "Nemo mobile specific GStreamer interfaces"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += "gstreamer1.0"

PV = "1.0.0+gitr${SRCPV}"
PR = "r0"

SRC_URI = "git://github.com/foolab/nemo-gst-interfaces.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

SRCREV = "94e28f45df1cf7e94e58114892b2b4314e9415c2"

inherit autotools
