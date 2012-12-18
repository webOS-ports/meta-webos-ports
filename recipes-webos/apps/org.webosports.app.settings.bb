SUMMARY = "Settings app re-written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit webos_arch_indep
inherit enyojs_application

PV = "0.3.0+gitr${SRCPV}"
PR = "r1"

SRCREV = "860c32b7b4a7e07ac0f822bdd8682874dfe2430e"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"
