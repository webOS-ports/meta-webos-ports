DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRC_URI = "git://github.com/webOS-ports/libhybris;branch=webOS-ports/master;protocol=git"
SRCREV = "eddd65e379484b90cbd87909ae713034bfe4342b"
