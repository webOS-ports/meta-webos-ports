SUMMARY = "Testing app for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_cordova_application
inherit webos_filesystem_paths

PV = "0.1.0+git${SRCPV}"
SRCREV = "27e78a79ae4d17825378e897360c47a8832f840f"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${WEBOS_APPLICATION_TARGET_DIR}
    install -v -m 0644 ${S}/receiver.html ${D}${WEBOS_APPLICATION_TARGET_DIR}/
}
