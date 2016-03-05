DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRCREV = "0522457476240888d834035cc80fc43b8f77f13e"
