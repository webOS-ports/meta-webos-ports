SUMMARY = "The Nemo Mobile D-Bus QML Plugin allows you to access services on the system and session bus, as well as provide your own services."
SECTION = "libs"
LICENSE = "LGPL-2.1-only & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://src/plugin/plugin.cpp;beginline=1;endline=30;md5=022f74a2e53dfa65df371578a62d2be3 \
    file://license.bsd;md5=caa037e0975ee5862b72644673e7590c \
    file://license.lgpl;md5=cb8aedd3bced19bd8026d96a8b6876d7 \
"

DEPENDS = "qtbase qtdeclarative glib-2.0 dbus dbus-glib"

PV = "2.1.28+git${SRCPV}"
SRCREV = "3293bb85c4e6277012573f71d9ef3479a5842a07"

SRC_URI = " \
    git://github.com/herrie82/nemo-qml-plugin-dbus;branch=herrie/qt6;protocol=https \
"
S = "${WORKDIR}/git"

inherit qt6-qmake

PACKAGES += "${PN}-test"

FILES:${PN}-test += "${base_prefix}/opt/tests"
FILES:${PN} += "${OE_QMAKE_PATH_QML}/Nemo/DBus"
