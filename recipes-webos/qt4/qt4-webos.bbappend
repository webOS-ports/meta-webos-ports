FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 7}"

# NOTE: We're disabling accelerated graphics support with the
# add-tuna-support-for-palm-plugin.patch here. It will force the plugin to use plain
# framebuffer graphic rendering. If we want to enable accelerated graphics later again we
# have to remove this patch and the palm plugin switches back to default rendering (which
# is using eglfs).
SRC_URI_append_tuna = " \
    file://dont-link-against-libhid.patch \
    file://add-tuna-support-for-palm-plugin.patch"

DEPENDS_append_tuna = " virtual/egl"

do_install_append_tuna() {
    oe_libinstall -C ${PALM_BUILD_DIR}/lib/ -so libQtOpenGL ${D}/usr/lib

    # NOTE: We can't use accelerated graphics yet but we install QML shader support
    # anyway to have it in place once we can use the PowerVR chip inside the device.
    install -d ${D}/usr/plugins/imports/Qt/labs/shaders
    install -m 555 ${PALM_BUILD_DIR}/imports/Qt/labs/shaders/* ${D}/usr/plugins/imports/Qt/labs/shaders/
}
