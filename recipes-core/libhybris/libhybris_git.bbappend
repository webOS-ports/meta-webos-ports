DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRC_URI = "git://github.com/webOS-ports/libhybris;branch=master;protocol=git"
SRCREV = "5308acb19e7487c65aa0651ddc058e7d855c5d3d"
