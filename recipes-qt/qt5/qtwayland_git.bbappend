SRC_URI = "git://github.com/webOS-ports/qtwayland;branch=webOS-ports/master-next;protocol=git"
SRCREV = "56c40e2665313cd3f16371bdd23a573362f35f04"

# don't remove create_cmake
EXTRA_QMAKEVARS_POST_remove = "CONFIG-=create_cmake"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
