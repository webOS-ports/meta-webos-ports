FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"
EXTRA_OECONF_append_aarch64 += " \ 
    --enable-arch=arm64 \
"
SRCREV = "3cda04985dab5f46d4c0f2067d2aab61362ee4b7"
