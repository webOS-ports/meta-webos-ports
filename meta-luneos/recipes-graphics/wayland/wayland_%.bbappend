
# remove provided `libwayland-egl` library in favour of the version in `libhybris`

do_install_append() {
    if ${@oe.utils.conditional('PREFERRED_PROVIDER_virtual/egl', 'libhybris', 'true', 'false', d)} ; then
        rm -rvf ${D}${libdir}/libwayland-egl*
        rm -rvf ${D}${libdir}/pkgconfig/wayland-egl.pc
    fi
}
