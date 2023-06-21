SUMMARY = "Qt 5/6 Library for ConnMan"
HOMEPAGE = "https://github.com/sailfishos/libconnman-qt"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://libconnman-qt/clockmodel.h;endline=8;md5=ea9f724050803f15d2d900ce3c5dac88"
DEPENDS += "qtbase qtdeclarative"

VERSION = "1.3.3"
PV = "${VERSION}+git${SRCPV}"

SRCREV = "16e1498c1de653f71391a0cafb47251be9355b6c"
SRC_URI = "git://github.com/sailfishos/libconnman-qt.git;protocol=https;branch=master \
    file://0001-connman_vpn_manager.xml-Fix-build-with-Qt-6.5.patch \
"

S = "${WORKDIR}/git"

inherit pkgconfig
inherit qt6-qmake

EXTRA_QMAKEVARS_PRE = "CONFIG+=no-module-prefix VERSION=${VERSION}"

RDEPENDS:${PN} += "connman"

do_install:append() {
    if ls ${D}${libdir}/pkgconfig/connman-qt5.pc >/dev/null 2>/dev/null; then
        sed -i "s@-L${STAGING_LIBDIR}@-L\${libdir}@g" ${D}${libdir}/pkgconfig/connman-qt5.pc
    fi
}
FILES:${PN} += " \
    ${OE_QMAKE_PATH_QML}/Connman/ \
"
FILES:${PN}-dev += " \
    ${libdir}/libconnman-qt6.prl \
"
