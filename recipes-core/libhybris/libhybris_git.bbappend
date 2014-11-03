DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRC_URI = "git://github.com/webOS-ports/libhybris;branch=webOS-ports/master-next;protocol=git"
SRCREV = "69d82b1aee617388c90c1888e1c5b5faf3a6ed1b"
