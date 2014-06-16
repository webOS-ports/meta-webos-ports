DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRC_URI = "git://github.com/webOS-ports/libhybris;branch=webOS-ports/master;protocol=git"
SRCREV = "ae46ef60dded67343c55d13764f0df42de992577"
