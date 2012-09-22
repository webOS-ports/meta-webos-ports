FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 5}"

SRC_URI_append_tuna = " \
    file://fix-webos-platform-plugin-compilation.patch \
    file://fix-webos-platform-plugin-configuration.patch"

DEPENDS_append_tuna = " virtual/egl"

do_install_append() {
    if [ "${MACHINE}" = "tuna" ]; then
        install -m 555 ${PALM_BUILD_DIR}/plugins/platforms/libqpalm.so ${D}${libdir}
        # oe_libinstall -C ${PALM_BUILD_DIR}/plugins/platforms -so libqwebos ${D}/usr/lib

        install -d ${D}/usr/plugins/platforms
        install -m 555 ${PALM_BUILD_DIR}/plugins/platforms/*.so ${D}/usr/plugins/platforms/

        install -m 555 ${PALM_BUILD_DIR}/plugins/platforms/libqpalm.so ${STAGING_LIBDIR}/libqpalm.so
        # install -m 555 ${PALM_BUILD_DIR}/plugins/platforms/libqwebos.so ${STAGING_LIBDIR}/libqwebos.so
    fi
}
