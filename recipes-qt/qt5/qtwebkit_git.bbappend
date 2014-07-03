DEPENDS += "luna-service2"

# Don't use qtmultimedia which is set by default PACKAGECONFIG
PACKAGECONFIG = "gstreamer qtlocation qtsensors"

SRC_URI = "git://github.com/webOS-ports/qtwebkit;branch=webOS-ports/master-next;protocol=git"
SRCREV = "d3852761f43f2fa7d999ac714933157d334b54a6"

THUMB_SUPPORT = " QMAKE_CXXFLAGS+=-mthumb"
# Build breaks for qemuarm with errors while building JavaScriptCore:
# | {standard input}: Assembler messages:
# | {standard input}:90: Error: invalid immediate: 983040 is out of range
# | {standard input}:90: Error: value of 983040 too large for field of 2 bytes at 44
# [...]
THUMB_SUPPORT_qemuarm = ""

EXTRA_QMAKEVARS_PRE_arm_append = " ${THUMB_SUPPORT}"
