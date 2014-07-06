DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRC_URI = "git://github.com/webOS-ports/libhybris;branch=webOS-ports/master;protocol=git"
SRCREV = "253a57741890df90092e4ab4dce543804f03919b"
