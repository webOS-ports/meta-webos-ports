SUMMARY = "Nemo GStreamer interfaces and buffer metadata used by gst-droid"
DESCRIPTION = "Small GStreamer support libraries from SailfishOS: the \
nemo-gstreamer-interfaces video texture/EGL interfaces and the \
nemo-gstreamer-meta buffer metadata. Needed to build gst-droid."
HOMEPAGE = "https://github.com/sailfishos/nemo-gst-interfaces"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base"

PV = "0.20211122+git"
SRCREV = "f5e90300ab0e2cfea1739392cc47c7b62f9c1700"
SRC_URI = "git://github.com/sailfishos/nemo-gst-interfaces.git;branch=master;protocol=https"

inherit autotools pkgconfig
