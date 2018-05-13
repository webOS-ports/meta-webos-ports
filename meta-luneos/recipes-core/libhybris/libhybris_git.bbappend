DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"
EXTRA_OECONF_append_aarch64 = " \ 
    --enable-arch=arm64 \
"
