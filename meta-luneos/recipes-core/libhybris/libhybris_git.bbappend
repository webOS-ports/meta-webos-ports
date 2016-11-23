DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRCREV = "d4741e6675582c248228a41f4c1616fbf39fd3e6"
