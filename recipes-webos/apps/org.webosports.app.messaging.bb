SUMMARY = "Messaging app written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "0.0.1-1_db3fb80245d9e40bcd4940f46c14179cd9a9de0c"

inherit webos_public_repo
inherit webos_enyojs_application
inherit webos_enhanced_submissions
inherit webos_component

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git/app"
