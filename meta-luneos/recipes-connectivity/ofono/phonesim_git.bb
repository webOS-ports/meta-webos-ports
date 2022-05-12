SUMMARY = "Phone Simulator for modem testing"
SECTION = "devel"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "qtbase qtbase-native"

SRCREV = "a7c844d45b047b2dae5b0877816c346fce4c47b9"
PV = "1.21+git${SRCPV}"

SRC_URI = "git://git.kernel.org/pub/scm/network/ofono/phonesim.git;branch=master \
"
S = "${WORKDIR}/git"

#    file://0001-Port-to-qt5.patch 
#    file://0002-Fix-random-build-failure.patch 
#    file://0003-configure.ac-use-gnu-11-to-fix-build-with-Qt-5.7.patch
#    file://0004-Fix-build-with-Qt-5.8.patch \
#    file://0005-configure.ac-fix-checking-for-host_bins-variable.patch 


export QMAKE_PATH_HOST_BINS = "${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}"

inherit autotools pkgconfig
#inherit qmake5_paths
inherit webos_qmake6_paths

CXXFLAGS += "-fPIC"
