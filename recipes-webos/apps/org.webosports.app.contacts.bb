SUMMARY = "Contacts app re-written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_enyojs_application
inherit webos_application

PV = "0.0.1+gitr${SRCPV}"

# For compatibility reasons we have to name the app internally as the old palm contacts
# app. Maybe possible to switch that later.
WEBOS_APPLICATION_NAME = "com.palm.app.contacts"

SRCREV = "2326890f273e381166e8c0f457ffb86e2fab1a49"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"
