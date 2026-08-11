DESCRIPTION = "Qt 6 bindings for the ofono dbus API for Jolla's oFono extensions"
SECTION = "libs"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=05ec901d0fb5d274579e113de1fea001"

# extra-cmake-modules is a build-time requirement: upstream switched the .pc file
# over to ECMGeneratePkgConfigFile in 92eb36d, and CMakeLists.txt does a hard
# find_package(ECM REQUIRED NO_MODULE).
DEPENDS += "qtbase qtdeclarative libqofono extra-cmake-modules"

SRCREV = "402588d07d6084d077ad095d3f7fd70cf1d29907"
SRC_URI = "git://github.com/sailfishos/libqofonoext.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.0.35"

# As for libqofono, upstream is CMake-only now (abf67a6/b43e54a). Both the Qt major
# version and the project version are cache variables that default to Qt5 / 1.0.32,
# so pass them explicitly. With QT_MAJOR_VERSION=6 upstream sets QTVERSION_SUFFIX
# to "-qt6", giving libqofonoext-qt6.so, ${includedir}/qofonoext-qt6 and
# qofonoext-qt6.pc, so the old qofono-qt5.pc sed fixup is gone.
inherit qt6-cmake pkgconfig

EXTRA_OECMAKE = " \
    -DQT_MAJOR_VERSION=6 \
    -DLIBQOFONOEXT_VERSION=${PV} \
"

# Same QML path problem as libqofono: upstream hardcodes ${CMAKE_INSTALL_LIBDIR}/qt6/qml
# while meta-qt6's import path is QT6_INSTALL_QMLDIR (${libdir}/qml), so the plugin
# has to be relocated or org.nemomobile.ofono cannot be imported.
do_install:append() {
    if [ -d ${D}${libdir}/qt6/qml ]; then
        install -d ${D}${QT6_INSTALL_QMLDIR}
        cp -a ${D}${libdir}/qt6/qml/. ${D}${QT6_INSTALL_QMLDIR}/
        rm -rf ${D}${libdir}/qt6
    fi
}

FILES:${PN} += " \
    ${QT6_INSTALL_QMLDIR}/org/nemomobile/ofono/libqofonoextdeclarative.so \
    ${QT6_INSTALL_QMLDIR}/org/nemomobile/ofono/qmldir \
    ${QT6_INSTALL_QMLDIR}/org/nemomobile/ofono/plugins.qmltypes \
"
