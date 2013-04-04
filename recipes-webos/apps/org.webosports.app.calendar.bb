SUMMARY = "Calendar app re-written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_enyojs_application
inherit webos_cordova_application

PV = "0.0.1+gitr${SRCPV}"

SRCREV = "5c1db766ce0ce50929010f787ea995539f66d2a3"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"
