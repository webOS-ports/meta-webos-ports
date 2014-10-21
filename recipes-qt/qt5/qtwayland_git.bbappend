SRC_URI = "git://github.com/webOS-ports/qtwayland;branch=webOS-ports/master-next;protocol=git"
SRCREV = "620d093e6f0c4bae36ab569f5ce7d6045dbd4002"

# don't remove create_cmake
EXTRA_QMAKEVARS_POST_remove = "CONFIG-=create_cmake"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
