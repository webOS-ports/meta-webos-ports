FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 12}"

SRC_URI_append_armv7a = " \
    file://dont-link-against-libhid.patch \
    file://keyboard-support-compilation-fix.patch"

DEPENDS_append_armv7a = " virtual/egl"

# Enable dbus support needed for some components inside webos-ports
DEPENDS += "dbus"
QT_CONFIG_FLAGS += "-dbus"

do_install_append() {
    oe_libinstall -C ${PALM_BUILD_DIR}/lib/ -so libQtDBus ${D}/usr/lib
}

do_install_append_armv7a() {
    oe_libinstall -C ${PALM_BUILD_DIR}/lib/ -so libQtOpenGL ${D}/usr/lib

    # NOTE: We can't use accelerated graphics yet but we install QML shader support
    # anyway to have it in place once we can use the PowerVR chip inside the device.
    install -d ${D}/usr/plugins/imports/Qt/labs/shaders
    install -m 555 ${PALM_BUILD_DIR}/imports/Qt/labs/shaders/* ${D}/usr/plugins/imports/Qt/labs/shaders/
}

# NOTE: luna-sysmgr has to be linked against libqpalm.so and as it's just a plugin it
# can't be installed in ${libdir} path as part of the default ${PN} package. Cause of this
# we put it into a separate package and make it a runtime dependency of the ${PN} package.
# Once upstream change luna-sysmgr to not link against libqpalm anymore this can be
# removed.
PACKAGES =+ "${PN}-support"
FILES_${PN}-support = "${libdir}/libqpalm.so"
RDEPENDS_${PN} += "${PN}-support"
