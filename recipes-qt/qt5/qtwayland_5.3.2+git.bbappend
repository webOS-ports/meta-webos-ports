SRC_URI = "git://github.com/webOS-ports/qtwayland;branch=webOS-ports/master-next;protocol=git"
SRCREV = "4600a18b90740bea57ad5f027aedb18641aec969"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://0001-Add-cmake-test-for-QtCompositor-and-disable-cmake-fi.patch"

# don't remove create_cmake
EXTRA_QMAKEVARS_POST_remove = "CONFIG-=create_cmake"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/wayland-graphics-integration/*/.debug"
