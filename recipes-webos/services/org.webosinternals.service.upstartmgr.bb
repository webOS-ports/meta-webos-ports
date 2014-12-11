SUMMARY = "Upstart management service for Open webOS"
SECTION = "webos/services"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 mjson"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

WEBOS_COMPONENT_VERSION = "0.1.0"
PV = "${WEBOS_COMPONENT_VERSION}+git${SRCPV}"
WEBOS_SUBMISSION = "0"

SRCREV = "ba7941c5ca2c3c13f8b149dc998354174d8241f9"
SRC_URI = "git://github.com/webOS-ports/${PN};branch=master;protocol=git"
S = "${WORKDIR}/git"
