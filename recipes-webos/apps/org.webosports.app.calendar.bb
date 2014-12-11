SUMMARY = "Calendar app re-written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_cordova_application
inherit webos_application

PV = "0.0.1+gitr${SRCPV}"

SRCREV = "6c1e757341badc990b5df7f4142b17b72a957da4"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"
