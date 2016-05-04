SUMMARY = "Phone Simulator for modem testing"
SECTION = "devel"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "qtbase qtscript qtxmlpatterns"

SRC_URI = "https://www.kernel.org/pub/linux/network/ofono/phonesim-${PV}.tar.xz"
SRC_URI[md5sum] = "44252d82a19a1c35d70160a6fbc965a1"
SRC_URI[sha256sum] = "8a858acdb99bfc928ba16c8d983103af198bc0aa0e9101477d343361628d95d9"
SRC_URI += " file://0001-Port-to-qt5.patch \
    file://qt-5.7.gnu++11.patch \
"

inherit autotools pkgconfig
inherit qmake5_paths

do_configure_append() {
    sed -i -e s:/usr/bin/qt5/uic:${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/uic:g ${B}/Makefile
    sed -i -e s:/usr/bin/qt5/moc:${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/moc:g ${B}/Makefile
}

CXXFLAGS += "-fPIC"
