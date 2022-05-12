SUMMARY = "Phone Simulator for modem testing"
SECTION = "devel"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "qtbase qtscript qtbase-native"

SRCREV = "7664fdc37d3bc072ea8e9a5d92cac24aa1d27ddd"
PV = "1.21+git${SRCPV}"

SRC_URI = "git://git.kernel.org/pub/scm/network/ofono/phonesim.git;branch=master \
    file://0001-Port-to-qt5.patch \
    file://0002-Fix-random-build-failure.patch \
    file://0003-configure.ac-use-gnu-11-to-fix-build-with-Qt-5.7.patch \
    file://0004-Fix-build-with-Qt-5.8.patch \
    file://0005-configure.ac-fix-checking-for-host_bins-variable.patch \
"
S = "${WORKDIR}/git"

export QMAKE_PATH_HOST_BINS = "${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}"

inherit autotools pkgconfig
inherit qt6-paths

CXXFLAGS += "-fPIC"
