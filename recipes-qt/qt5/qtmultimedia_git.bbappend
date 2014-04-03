SRC_URI = "git://github.com/webOS-ports/qtmultimedia.git;branch=webOS-ports/master;protocol=git"
S = "${WORKDIR}/git"
SRCREV = "ba3f60d6d786f5f1842e7dc176d1f7ce7cf47e56"

# FIXME: Enable mir gstreamer audio/video implementation once we are starting to use it
# EXTRA_QMAKEVARS_PRE += "CONFIG+=mir"

# Sadly we have to pass this here in order to make the new gstreamer 1.0/0.10 detection
# algorithm work after we modified it in qtmultimedia.inc which we can't revert here ..
EXTRA_QMAKEVARS_PRE += "CONFIG+=OE_GSTREAMER010_ENABLED"

DEPENDS += "gstreamer1.0 gstreamer1.0-plugins-base"
