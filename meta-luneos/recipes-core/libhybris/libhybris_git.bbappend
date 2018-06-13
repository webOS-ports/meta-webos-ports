FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

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
