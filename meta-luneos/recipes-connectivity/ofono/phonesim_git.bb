SUMMARY = "Phone Simulator for modem testing"
SECTION = "devel"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "qtbase qtbase-native qtdeclarative qt5compat"

SRCREV = "2a2d508bc7ff4ca6dd5b8df7927c6e883731d23b"
PV = "1.21+git${SRCPV}"

SRC_URI = "git://github.com/herrie82/phonesim.git;protocol=https;branch=herrie/qt6 \
"
S = "${WORKDIR}/git"

inherit qt6-cmake
inherit webos_cmake
inherit pkgconfig
