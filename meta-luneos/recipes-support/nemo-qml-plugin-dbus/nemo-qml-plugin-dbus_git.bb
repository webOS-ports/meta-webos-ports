SUMMARY = "The Nemo Mobile D-Bus QML Plugin allows you to access services on the system and session bus, as well as provide your own services."
SECTION = "libs"
LICENSE = "LGPL-2.1-only & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://src/plugin/plugin.cpp;beginline=1;endline=30;md5=3b9f77a89aa84d67fac89051acacc1d5 \
    file://license.bsd;md5=caa037e0975ee5862b72644673e7590c \
    file://license.lgpl;md5=cb8aedd3bced19bd8026d96a8b6876d7 \
"

DEPENDS = "qtbase qtdeclarative glib-2.0 dbus dbus-glib"

PV = "2.1.39+git"
SRCREV = "32d1696a0c81f25deb278e76fc919e8da990d26b"

# 0001-Fix-build-with-Qt-6.5.patch was dropped at 2.1.39: upstream's "Allow build
# with Qt 6" (2dcadbf) made signalHandler() build both ways by constructing the
# QGenericArgument from QMetaType under Qt 6, which covers the same Q_ARG error.
SRC_URI = "git://github.com/sailfishos/nemo-qml-plugin-dbus;branch=master;protocol=https"

inherit qt6-qmake

PACKAGES += "${PN}-test"

FILES:${PN}-test += "${base_prefix}/opt/tests"
FILES:${PN} += "${OE_QMAKE_PATH_QML}/Nemo/DBus"
