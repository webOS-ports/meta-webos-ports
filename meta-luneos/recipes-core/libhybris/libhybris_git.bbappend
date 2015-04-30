DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"

SRCREV = "c42b4bf5ad392fdc34227d46dfc5caef05a01ba7"
