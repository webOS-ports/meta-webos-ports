SUMMARY = "Messaging app written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "0.0.1-2_e8c096f3b8690b29eb2a02a4f3dcc7ea84d40587"

inherit webos_public_repo
inherit webos_enyojs_application
inherit webos_enhanced_submissions

SRC_URI = "git://github.com/webOS-ports/org.webosports.messaging;protocol=git"
S = "${WORKDIR}/git/app"
