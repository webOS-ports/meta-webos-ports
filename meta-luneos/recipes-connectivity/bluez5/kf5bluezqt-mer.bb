SUMMARY = "Qt 5 and QML bindings for the Bluez5 dbus API"
SECTION = "libs"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://bluez-qt/COPYING.LIB;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "qtbase qtdeclarative qtbase-native"

inherit qt6-qmake

PV = "5.24.0+git"
SRCREV = "e611adc6dce026e32aef6db902cfa4ee99d00265"

SRC_URI = "git://github.com/sailfishos/kf5bluezqt.git;protocol=https;branch=master \
           file://qt_BluezQt.pri \
           file://0001-minimal-migration-to-Qt6.patch \
           file://0001-Update-D-Bus-xml-files-to-use-Out-for-signal-type-Qt.patch \
"

S = "${WORKDIR}/git"
B = "${S}"

EXTRA_QMAKEVARS_PRE += "KF5BLUEZQT_BLUEZ_VERSION=5"

do_configure:prepend() {
    sed -i "s@(qdbusxml2cpp@(${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/qdbusxml2cpp@g" ${S}/bluez-qt/src/interfaces/interfaces.pri
}

do_install:append() {
    mkdir -p ${D}${OE_QMAKE_PATH_ARCHDATA}/mkspecs/modules/
    cp ${UNPACKDIR}/qt_BluezQt.pri ${D}${OE_QMAKE_PATH_ARCHDATA}/mkspecs/modules/
}

FILES:${PN} += " \
		${OE_QMAKE_PATH_QML}/org/kde/bluezqt \
		"

FILES:${PN}-dev += " \
		${OE_QMAKE_PATH_ARCHDATA}/mkspecs/modules/ \
		"

RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
"
