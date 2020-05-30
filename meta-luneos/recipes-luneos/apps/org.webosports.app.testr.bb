SUMMARY = "Testing app for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_cordova_application
inherit webos_filesystem_paths
inherit webos_app

PV = "0.1.0+git${SRCPV}"
SRCREV = "ed4b987687bda5530d89cf832f5de8182ba6d0f3"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${WEBOS_APPLICATION_TARGET_DIR}
    install -v -m 0644 ${S}/receiver.html ${D}${WEBOS_APPLICATION_TARGET_DIR}/
}

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
