DESCRIPTION = "Qt 6 bindings for the ofono dbus API for Jolla's oFono extensions"
SECTION = "libs"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=05ec901d0fb5d274579e113de1fea001"

DEPENDS += "qtbase qtdeclarative libqofono"

SRCREV = "1893185f2124ef5487fc684f9e69237b8551f4c4"
SRC_URI = "git://github.com/sailfishos/libqofonoext.git;protocol=https;branch=master"

PV = "1.0.32"

inherit pkgconfig qt6-qmake

do_install:append() {
    if ls ${D}${libdir}/pkgconfig/qofono-qt5.pc >/dev/null 2>/dev/null; then
        sed -i "s@-L${STAGING_LIBDIR}@-L\${libdir}@g" ${D}${libdir}/pkgconfig/qofono-qt5.pc
    fi
}

PACKAGES += "${PN}-tests"

FILES:${PN} += " \
    ${OE_QMAKE_PATH_QML}/org/nemomobile/ofono/qmldir \
    ${OE_QMAKE_PATH_QML}/org/nemomobile/ofono/plugins.qmltypes \
    ${OE_QMAKE_PATH_QML}/org/nemomobile/ofono/libqofonoextdeclarative.so \
"
