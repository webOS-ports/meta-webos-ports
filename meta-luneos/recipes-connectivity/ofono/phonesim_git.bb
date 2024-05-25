SUMMARY = "Phone Simulator for modem testing"
SECTION = "devel"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "qtbase qtbase-native qtdeclarative qt5compat qtdeclarative-native"

SRCREV = "a55ada223b31ce9a69d0d225ec2aa0b8f311e401"
PV = "2.0+git"

SRC_URI = "git://git.kernel.org/pub/scm/network/ofono/phonesim.git;protocol=https;branch=master \
        file://phonesim.service \
        file://0001-Phonesim-Port-to-CMake-and-Qt6.patch \
        file://0002-default.xml-LuneOS-Branding.patch \
"
S = "${WORKDIR}/git"

inherit qt6-cmake
inherit webos_cmake
inherit pkgconfig
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "phonesim.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/phonesim.service ${D}${systemd_unitdir}/system/
}
