SUMMARY = "Qt and QML bindings for the BlueZ 5 D-Bus API"
DESCRIPTION = "KDE Frameworks' BluezQt, the upstream of the kf5bluezqt fork this \
replaces. That fork tracked KF5 5.112 and needed a local 'minimal migration to \
Qt6' patch to build here at all; upstream has supported Qt6 since KF6, so the \
patch is not needed and neither is the fork."
SECTION = "libs"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSES/LGPL-2.1-or-later.txt;md5=2a4f4fd2128ea2f65047ee63fbca9f68"

# Pinned to the release matching the extra-cmake-modules in this layer. ECM and
# the frameworks are released together and the CMake config checks the version,
# so the two want to move as a pair.
PV = "6.28.0"
SRCREV = "4aeac955487f7aaad0f789a97692a31e1a92a502"

SRC_URI = "git://invent.kde.org/frameworks/bluez-qt.git;branch=master;protocol=https \
           file://qt_BluezQt.pri \
"

# qtdeclarative-native supplies the QML tooling CMake looks for (Qt6QmlTools:
# qmlcachegen, qmltyperegistrar), which is not in the target sysroot.
DEPENDS = "qtbase qtdeclarative extra-cmake-modules qtbase-native qtdeclarative-native"

inherit qt6-cmake

EXTRA_OECMAKE += " \
    -DBUILD_TESTING=OFF \
    -DBUILD_QCH=OFF \
"

# The KDE build ships only a CMake config, but luneos-components is a qmake
# recipe that does "QT += BluezQt" behind a qtHaveModule(BluezQt) guard - and
# that guard fails silently, so without a qmake module file its whole Bluetooth
# subdir is just dropped from SUBDIRS and the build still succeeds. The fork
# carried the same hand-written .pri; this is the KF6 copy of it. The header
# layout is unchanged between the two (${includedir}/KF<n>/BluezQt/bluezqt/*.h)
# and the QML types are still exported as 1.0, so consumers need no source
# change - only the library name moves KF5BluezQt -> KF6BluezQt.
do_install:append() {
    install -d ${D}${libdir}/mkspecs/modules
    install -m 0644 ${UNPACKDIR}/qt_BluezQt.pri ${D}${libdir}/mkspecs/modules/
}

# The QML plugin and the udev rules the fork also shipped. KDE_INSTALL_QMLDIR
# resolves to ${libdir}/qml here, not the ${libdir}/qt6/qml a Qt-native build
# would use.
FILES:${PN} += " \
    ${libdir}/qml \
    ${nonarch_base_libdir}/udev/rules.d \
    ${datadir}/qlogging-categories6 \
"

FILES:${PN}-dev += "${libdir}/mkspecs/modules"

RPROVIDES:${PN} = "kf5bluezqt-mer"
RREPLACES:${PN} = "kf5bluezqt-mer"
RCONFLICTS:${PN} = "kf5bluezqt-mer"
