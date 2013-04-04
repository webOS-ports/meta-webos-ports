SUMMARY = "Open webOS Telephony daemon"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 cjson glib-2.0"

inherit webos_component
inherit webos_public_repo
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus

WEBOS_COMPONENT_VERSION = "0.1.0"
PV = "${WEBOS_COMPONENT_VERSION}+gitr${SRCPV}"
WEBOS_SUBMISSION = "0"
SRCREV = "c9e3c1b478186b95a401aa4822c49575dd6b152e"

SRC_URI = "git://github.com/webOS-ports/${PN};branch=master;protocol=git"
S = "${WORKDIR}/git"
