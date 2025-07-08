DESCRIPTION = "Qt 6 bindings for the ofono dbus API for Jolla's oFono extensions"
SECTION = "libs"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=9d8e973205911d7558cbc57aad8e9359"

DEPENDS += "qtbase qtdeclarative libqofono"

SRCREV = "af438b503af3875a4f82f0fb99278a1b2e39414d"
SRC_URI = "git://github.com/sailfishos/libqofonoext.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.029+git"

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
