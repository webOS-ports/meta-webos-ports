SUMMARY = "Qt 5 and QML bindings for the Bluez5 dbus API"
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://bluez-qt/COPYING.LIB;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "qtbase qtdeclarative qtbase-native"

inherit qmake5

PV = "5.24.0+git${SRCPV}"
SRCREV = "e611adc6dce026e32aef6db902cfa4ee99d00265"

SRC_URI = "git://git.merproject.org/mer-core/kf5bluezqt.git;protocol=https \
           file://qt_BluezQt.pri"
	   
S = "${WORKDIR}/git"
B = "${S}"

EXTRA_QMAKEVARS_PRE += "KF5BLUEZQT_BLUEZ_VERSION=5"

do_configure_prepend() {
    sed -i "s@(qdbusxml2cpp@(${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/qdbusxml2cpp@g" ${S}/bluez-qt/src/interfaces/interfaces.pri
}

do_install_append() {
    mkdir -p ${D}${libdir}/qt5/mkspecs/modules/
    cp ${WORKDIR}/qt_BluezQt.pri ${D}${libdir}/qt5/mkspecs/modules/
}

FILES_${PN} += " \
		${libdir}/qt5/qml/org/kde/bluezqt \
		"

FILES_${PN}-dev += " \
		${libdir}/qt5/mkspecs/modules/ \
		"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
"
