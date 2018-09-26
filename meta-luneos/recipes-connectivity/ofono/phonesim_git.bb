SUMMARY = "Phone Simulator for modem testing"
SECTION = "devel"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "qtbase qtscript qtxmlpatterns qtbase-native"

SRCREV = "774958e133e0b53c87cfa0049b243e11874f3bae"
PV = "1.20+git${SRCPV}"

SRC_URI = "git://git.kernel.org/pub/scm/network/ofono/phonesim.git \
    file://0001-Port-to-qt5.patch \
    file://0002-Fix-random-build-failure.patch \
    file://0003-configure.ac-use-gnu-11-to-fix-build-with-Qt-5.7.patch \
    file://0004-Fix-build-with-Qt-5.8.patch \
"
S = "${WORKDIR}/git"

inherit autotools pkgconfig
inherit qmake5_paths

do_configure_append() {
    sed -i -e s:/usr/bin/qt5/uic:${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/uic:g ${B}/Makefile
    sed -i -e s:/usr/bin/qt5/moc:${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/moc:g ${B}/Makefile
}

CXXFLAGS += "-fPIC"
