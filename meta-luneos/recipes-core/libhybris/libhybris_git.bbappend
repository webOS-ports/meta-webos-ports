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
SRCREV = "4aa3379de1d911f00a86cb5643f7ff63dd48d7a6"
