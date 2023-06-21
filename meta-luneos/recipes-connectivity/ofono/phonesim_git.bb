SUMMARY = "Phone Simulator for modem testing"
SECTION = "devel"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "qtbase qtbase-native qtdeclarative qt5compat qtdeclarative-native"

SRCREV = "2a2d508bc7ff4ca6dd5b8df7927c6e883731d23b"
PV = "1.21+git${SRCPV}"

SRC_URI = "git://github.com/herrie82/phonesim.git;protocol=https;branch=herrie/qt6 \
           file://phonesim.service \
	   file://phonesim.conf \
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

    install -d ${D}${sysconfdir}/ofono
    install -m 0644 ${WORKDIR}/phonesim.conf ${D}${sysconfdir}/ofono/phonesim.conf
}
