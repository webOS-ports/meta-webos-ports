DESCRIPTION = "Qt bindings for the connman dbus API"
SECTION = "qt/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCREV = "190971080a36a89369be1d8ff79e9972b9672a90"
SRC_URI = "git://github.com/nemomobile/libconnman-qt.git;protocol=git;branch=master \
    file://0001-Don-t-use-MeeGo-as-prefix-in-order-to-make-this-a-co.patch"
S = "${WORKDIR}/git"

PV = "1.0.55+gitr${SRCPV}"

inherit qmake5

DEPENDS += "qtbase qtdeclarative"

# Set path of qt5 headers as qmake5_base.bbclass sets this to just ${includedir} but
# actually it is ${includedir}/qt5
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

FILES_${PN} += "${libdir}/qt5/qml/Connman"
FILES_${PN}-dev += "${libdir}/libconnman-qt5.prl ${libdir}/pkgconfig/connman-qt5.pc"
FILES_${PN}-dbg += "${libdir}/qt5/qml/Connman/.debug"
