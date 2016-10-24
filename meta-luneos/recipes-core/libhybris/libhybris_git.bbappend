DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRC_URI += "file://0001-fix-compilation-with-gcc6.patch" 

SRCREV = "63b253bd9985c84534acb42a89e8e5bd1db823e1"
