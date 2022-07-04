DESCRIPTION = "Qt 5 bindings for the ofono dbus API"
SECTION = "libs"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += "qtbase qtdeclarative"

SRCREV = "8d843d35f682a83bb1fec6c92595e0753ffe0079"

SRC_URI = "git://github.com/sailfishos/libqofono.git;protocol=https;branch=master \
"
S = "${WORKDIR}/git"

PV = "0.113+gitr${SRCPV}"

inherit qt6-qmake

do_install:append() {
    if ls ${D}${libdir}/pkgconfig/qofono-qt5.pc >/dev/null 2>/dev/null; then
        sed -i "s@-L${STAGING_LIBDIR}@-L\${libdir}@g" ${D}${libdir}/pkgconfig/qofono-qt5.pc
    fi
}

PACKAGES += "${PN}-tests"

FILES:${PN}-tests = " \
    ${libdir}/libqofono-qt5/tests/tst_* \
    /opt/examples/libqofono-qt5/ \
    /opt/tests/libqofono-qt5 \
"
FILES:${PN} += " \
    ${OE_QMAKE_PATH_QML}/MeeGo/QOfono/qmldir \
    ${OE_QMAKE_PATH_QML}/MeeGo/QOfono/plugins.qmltypes \
    ${OE_QMAKE_PATH_QML}/MeeGo/QOfono/libQOfonoQtDeclarative.so \
"
FILES:${PN}-dev += " \
    ${OE_QMAKE_PATH_ARCHDATA}/mkspecs \
    ${libdir}/libqofono-qt5.prl \
    ${datadir}/qt5/mkspecs \
"
