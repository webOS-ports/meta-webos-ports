SUMMARY = "Contacts app re-written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_application

PV = "0.0.1+gitr${SRCPV}"

# For compatibility reasons we have to name the app internally as the old palm contacts
# app. Maybe possible to switch that later.
WEBOS_APPLICATION_NAME = "com.palm.app.contacts"

SRCREV = "d7e23bcf51d958f4af1d79cc9cd1513fe6869bbd"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"
