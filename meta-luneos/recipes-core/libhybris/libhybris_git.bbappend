FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Fix_test_sensors_printf.patch;striplevel=2 \
    file://0002-tests-make-test_camera-use-wayland.patch;striplevel=2 \
"

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
SRCREV = "76f83fcd8a4c5aa43d4342d00af6242a20789a02"
