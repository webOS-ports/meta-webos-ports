SRC_URI = "git://github.com/webOS-ports/qtwayland;branch=webOS-ports/master-next;protocol=git"
SRCREV = "4600a18b90740bea57ad5f027aedb18641aec969"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
