DESCRIPTION = "Qt bindings for the connman dbus API"
SECTION = "qt/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCREV = "8d4580a55ca02b84fc3db374c6530e39c94e0d92"
SRC_URI = "git://git.merproject.org/mer-core/libconnman-qt.git;protocol=git;branch=master \
    file://0001-Don-t-use-MeeGo-as-prefix-in-order-to-make-this-a-co.patch"
S = "${WORKDIR}/git"

PV = "1.0.98+gitr${SRCPV}"

inherit qmake5

DEPENDS += "qtbase qtdeclarative"

FILES_${PN} += "${libdir}/qt5/qml/Connman"
FILES_${PN}-dev += "${libdir}/libconnman-qt5.prl ${libdir}/pkgconfig/connman-qt5.pc"
FILES_${PN}-dbg += "${libdir}/qt5/qml/Connman/.debug"
