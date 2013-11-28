DESCRIPTION = "Qt bindings for the connman dbus API"
SECTION = "qt/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCREV = "eba445b1a11ceabe6e475b2ff38011ed3f8e694c"
SRC_URI = "git://github.com/nemomobile/libconnman-qt.git;protocol=git;branch=master \
    file://0001-Don-t-use-MeeGo-as-prefix-in-order-to-make-this-a-co.patch"
S = "${WORKDIR}/git"

PV = "1.0.43+gitr${SRCPV}"

inherit qmake5

# Set path of qt5 headers as qmake5_base.bbclass sets this to just ${includedir} but
# actually it is ${includedir}/qt5
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

do_install_append() {
    # Install pkgconfig configuration file into correct place
    install -d ${D}${libdir}/pkgconfig
    mv ${D}${libdir}/connman-qt5.pc ${D}${libdir}/pkgconfig
}

FILES_${PN} += "${libdir}/qt5/qml/Connman"
FILES_${PN}-dev += "${libdir}/libconnman-qt5.prl ${libdir}/pkgconfig/connman-qt5.pc"
FILES_${PN}-dbg += "${libdir}/qt5/qml/Connman/.debug"
