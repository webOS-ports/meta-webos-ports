DESCRIPTION = "Qt 5 bindings for the ofono dbus API"
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += "qtbase"

SRCREV = "6566d6a2272c39278f89657a8f858e1de0c59523"
SRC_URI = "git://github.com/nemomobile/libqofono.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

PV = "0.49+gitr${SRCPV}"

inherit qmake5

PACKAGES += "{PN}-tests"
FILES_${PN}-dbg += " \
    /lib/libqofono-qt5/tests/.debug \
    ${libdir}/qt5/qml/MeeGo/QOfono/.debug"
FILES_${PN}-tests = " \
    /lib/libqofono-qt5/tests"
FILES_${PN} += " \
    ${libdir}/qt5/qml/MeeGo/QOfono/qmldir \
    ${libdir}/qt5/qml/MeeGo/QOfono/libQOfonoQtDeclarative.so"
FILES_${PN}-dev += " \
    ${datadir}/qt5/mkspecs \
    ${libdir}/libqofono-qt5.prl"
