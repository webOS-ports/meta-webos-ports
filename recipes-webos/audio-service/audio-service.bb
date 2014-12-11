SUMMARY = "Service interface to control pulseaudio through a ls2 based API."
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 cjson glib-2.0 pulseaudio"

WEBOS_VERSION = "0.1.0-10_c729e0bdea1539a9ba68fceb037bb9776f5bde78"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

SRC_URI = "git://github.com/webOS-ports/${PN};branch=master;protocol=git"
S = "${WORKDIR}/git"
