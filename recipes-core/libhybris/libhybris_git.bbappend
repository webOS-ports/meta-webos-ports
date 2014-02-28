DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRC_URI = "git://github.com/webOS-ports/libhybris;branch=webOS-ports/master;protocol=git"
SRCREV = "4825ed3ec9b720bf6807f82128dcf0031aa1a0b0"
