SUMMARY = "Android specific GStreamer plugin"
HOMEPAGE = "https://gitorious.org/gstreamer1-0/"
LICENSE = "LGPL-2.1"
LIC_FILES_CHECKSUM = "file://COPYING;md5="

PV = "1.0.0+gitr${SRCPV}"
PR = "r0"

DEPENDS = "gstreamer1.0 gstreamer1.9-plugins-base gstreamer1.0-plugins-bad"

SRC_URI = "git://gitorious.org/gstreamer1-0/gst-droid.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

SRCREV = "009bc7de9d84458d00b4009aaaaff33c0df7992d"

inherit autotools
