SUMMARY = "Open webOS Telephony daemon"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 cjson glib-2.0 luna-prefs"

WEBOS_VERSION = "0.1.0-1_3d544cdeba57d068ce17bc4bc57f0e036ecfc6dd"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_enhanced_submissions

SRC_URI = "git://github.com/webOS-ports/${PN};branch=master;protocol=git"
S = "${WORKDIR}/git"
