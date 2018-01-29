FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
    --with-default-hybris-ld-library-path=/usr/libexec/hal-droid/system/lib \
"
EXTRA_OECONF_append_aarch64 += " \ 
    --enable-arch=arm64 \
"
SRCREV = "36dc78baa0a3a006b39266014787f96458b301da"
