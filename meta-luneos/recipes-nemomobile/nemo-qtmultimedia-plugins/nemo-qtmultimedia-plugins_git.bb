SUMMARY = "Nemo QtMultimedia Plugins"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://src/videotexturebackend/videotexturebackend.cpp;beginline=1;endline=31;md5=345009371abdfb1df57032e6f41dbd26"

DEPENDS = "virtual/egl gstreamer1.0 nemo-gst-interfaces qtmultimedia"

PV = "0.0.0+gitr${SRCPV}"
SRCREV = "9d29103965d4f6913210184a0c99b0eb6e80633b"

SRC_URI = "git://github.com/foolab/nemo-qtmultimedia-plugins.git;protocol=git;branch=next"
S = "${WORKDIR}/git"

inherit qmake5

FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/*/*/.debug"
FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/*/*/"
