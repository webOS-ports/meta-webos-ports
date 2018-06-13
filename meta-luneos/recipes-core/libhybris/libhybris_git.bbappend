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
SRCREV = "76f83fcd8a4c5aa43d4342d00af6242a20789a02"

do_install_append() {
    rm -rvf ${D}/${libdir}/libwayland-egl*
    rm -rvf ${D}/${libdir}/pkgconfig/wayland-egl*
}
