SUMMARY = "Settings app written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_enyojs_application
inherit webos_cordova_application

PV = "0.3.0+gitr${SRCPV}"

SRCREV = "0986d218bd962ceef9f5747f0b05fcd9a4ccf9ca"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"
