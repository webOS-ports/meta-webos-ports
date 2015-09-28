SUMMARY = "The Nemo Mobile D-Bus QML Plugin allows you to access services on the system and session bus, as well as provide your own services."
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://src/declarativedbusinterface.h;beginline=1;endline=23;md5=cb3590d30ca09b91405bf5164edab8bf"

DEPENDS = "qtbase qtdeclarative glib-2.0 dbus dbus-glib"

PV = "2.0.7+gitr${SRCPV}"
SRCREV = "63febe3ce844c8f4ddb0903cf7084738a770e048"

SRC_URI = " \
    git://github.com/nemomobile/nemo-qml-plugin-dbus;branch=master;protocol=git \
"
S = "${WORKDIR}/git"

inherit qmake5

PACKAGES += "${PN}-test"

FILES_${PN}-test += "${base_prefix}/opt/tests"
FILES_${PN}-dbg += "${libdir}/qt5/qml/org/nemomobile/dbus/.debug"
FILES_${PN} += "${libdir}/qt5/qml/org/nemomobile/dbus"
