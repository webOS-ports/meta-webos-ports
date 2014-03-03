SUMMARY = "Service interface to control pulseaudio through a ls2 based API."
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 cjson glib-2.0 pulseaudio"

WEBOS_VERSION = "0.1.0-6_9bdfed8aa0c69d9399c6f843af7e14e5f41bdaea"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
inherit webos_systemd

SRC_URI = "git://github.com/webOS-ports/${PN};branch=master;protocol=git"
S = "${WORKDIR}/git"
