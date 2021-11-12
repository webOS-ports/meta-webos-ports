SUMMARY = "The Nemo Mobile D-Bus QML Plugin allows you to access services on the system and session bus, as well as provide your own services."
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://src/plugin/plugin.cpp;beginline=1;endline=30;md5=20b5c251f7a4322d5795c2ee74eed742"

DEPENDS = "qtbase qtdeclarative glib-2.0 dbus dbus-glib"

PV = "2.1.20+git${SRCPV}"
SRCREV = "43b4a8fb2cb377f0a4f1fd0a2a389e4650a00425"

SRC_URI = " \
    git://github.com/sailfishos/nemo-qml-plugin-dbus;branch=master;protocol=https \
"
S = "${WORKDIR}/git"

inherit qmake5

PACKAGES += "${PN}-test"

FILES:${PN}-test += "${base_prefix}/opt/tests"
FILES:${PN} += "${OE_QMAKE_PATH_QML}/Nemo/DBus"
