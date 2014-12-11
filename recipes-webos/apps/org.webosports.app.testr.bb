SUMMARY = "Testing app for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_cordova_application

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "2778aaf251b663874273bf69dd9b52a0b1bf2bec"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"
