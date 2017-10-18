SUMMARY = "The Nemo Mobile D-Bus QML Plugin allows you to access services on the system and session bus, as well as provide your own services."
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://src/plugin/plugin.cpp;beginline=1;endline=30;md5=20b5c251f7a4322d5795c2ee74eed742"

DEPENDS = "qtbase qtdeclarative glib-2.0 dbus dbus-glib"

PV = "2.1.12+gitr${SRCPV}"
SRCREV = "872b836a43eb9fb492bf0ca6fd57f9b7eafb17db"

SRC_URI = " \
    git://git.merproject.org/mer-core/nemo-qml-plugin-dbus;branch=master;protocol=git \
"
S = "${WORKDIR}/git"

inherit qmake5

PACKAGES += "${PN}-test"

FILES_${PN}-test += "${base_prefix}/opt/tests"
FILES_${PN}-dbg += "${libdir}/qt5/qml/Nemo/DBus/.debug"
FILES_${PN} += "${libdir}/qt5/qml/Nemo/DBus"
