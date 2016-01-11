SUMMARY = "Testing app for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_cordova_application
inherit webos_filesystem_paths

PV = "0.1.0+gitr${SRCPV}"
SRCREV = "80bb86d0aa1794861f3fa73ab211a3edfff91ea8"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${WEBOS_APPLICATION_TARGET_DIR}
    install -v -m 0644 ${S}/receiver.html ${D}${WEBOS_APPLICATION_TARGET_DIR}/
}
