FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-wayland-leave-a-lost-compositor-to-the-client.patch"

DEPENDS += "wayland"
EXTRA_OECONF += " \
    --enable-wayland \
    --with-default-egl-platform=wayland \
    --enable-trace \
    --enable-debug \
"
EXTRA_OECONF:append:aarch64 = " \ 
    --enable-arch=arm64 \
"
