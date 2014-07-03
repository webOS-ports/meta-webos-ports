DEPENDS += "luna-service2"

# Don't use qtmultimedia which is set by default PACKAGECONFIG
PACKAGECONFIG = "gstreamer qtlocation qtsensors"

SRC_URI = "git://github.com/webOS-ports/qtwebkit;branch=webOS-ports/master-next;protocol=git"
SRCREV = "d3852761f43f2fa7d999ac714933157d334b54a6"

EXTRA_QMAKEVARS_PRE_arm_append = " QMAKE_CXXFLAGS+=-mthumb"
