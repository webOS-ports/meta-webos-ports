FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Fix_test_sensors_printf.patch;striplevel=2 \
    file://0002-tests-make-test_camera-use-wayland.patch;striplevel=2 \
    file://0003-change-default-wayland-display.patch;striplevel=2 \
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
SRCREV = "3beca8ad9246e3fce3c47ef978c3b07f6c04284a"

do_install_append() {
    rm -rvf ${D}/${libdir}/libwayland-egl*
    rm -rvf ${D}/${libdir}/pkgconfig/wayland-egl*
}
