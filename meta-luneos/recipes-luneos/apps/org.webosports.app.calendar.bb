SUMMARY = "Calendar app re-written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_cordova_application
inherit webos_application
inherit webos_filesystem_paths

PV = "0.0.1+gitr${SRCPV}"
SRCREV = "87c38382e2e0f3da02cd478f81a5d614eb224c4b"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    # Drop the firstrun activity for now as we don't want it to be executed right now
    # unless we have support for this in the app.
    rm ${D}${webos_sysconfdir}/activities/org.webosports.app.calendar/org.webosports.app.calendar.firstrun
}
