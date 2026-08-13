DESCRIPTION = "Qt 6 bindings for the ofono dbus API"
SECTION = "libs"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += "qtbase qtdeclarative"

SRCREV = "c54ba898a2daca1b5884b59353d0d7922405a3e1"
PV = "0.130"

SRC_URI = "git://github.com/sailfishos/libqofono.git;protocol=https;branch=master"

# Upstream dropped qmake in b43e54a and builds with CMake only, so this no longer
# inherits qt6-qmake. QT_MAJOR_VERSION is a cache variable that still defaults to 5
# upstream, and LIBQOFONO_VERSION defaults to a stale 0.124 in CMakeLists.txt, so
# both have to be passed in or the library gets built for Qt5 and versioned wrong.
inherit qt6-cmake

EXTRA_OECMAKE = " \
    -DQT_MAJOR_VERSION=6 \
    -DLIBQOFONO_VERSION=${PV} \
"

PACKAGES += "${PN}-tests"

# Upstream's CMake hardcodes the QML plugin destination as
# ${CMAKE_INSTALL_LIBDIR}/qt6/qml, but meta-qt6 puts the import path at
# QT6_INSTALL_QMLDIR (${libdir}/qml), which is where the qmake build used to
# install via OE_QMAKE_PATH_QML and where every other QML module on the image
# lives. Left alone the plugin lands in a directory nothing scans, and QML fails
# with "Type TelephonyManager unavailable" at runtime with no build-time error.
do_install:append() {
    if [ -d ${D}${libdir}/qt6/qml ]; then
        install -d ${D}${QT6_INSTALL_QMLDIR}
        cp -a ${D}${libdir}/qt6/qml/. ${D}${QT6_INSTALL_QMLDIR}/
        rm -rf ${D}${libdir}/qt6
    fi
}

# The QML plugin is an unversioned .so, so claim it for the runtime package before
# the default -dev rules take it.
FILES:${PN} += " \
    ${QT6_INSTALL_QMLDIR}/QOfono/libQOfonoQtDeclarative.so \
    ${QT6_INSTALL_QMLDIR}/QOfono/qmldir \
    ${QT6_INSTALL_QMLDIR}/QOfono/plugins.qmltypes \
"

# The ofonotest example app installs alongside the unit tests, as it did under
# qmake. Note upstream's ofonotest/CMakeLists.txt installs main.qml with the file
# name repeated as the DESTINATION, so it lands as .../qml/ofonotest/main.qml/main.qml;
# the trailing-slash match covers that either way.
FILES:${PN}-tests = " \
    ${base_prefix}/opt/tests/libqofono-qt6 \
    ${base_prefix}/opt/examples/libqofono-qt6 \
"

# Public headers and the D-Bus XML that ships beside them both land under
# ${includedir}/qofono-qt6, which the default -dev packaging picks up.
