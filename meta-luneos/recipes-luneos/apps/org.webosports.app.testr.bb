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

PV = "0.1.0+git"
PE = "1"
SRCREV = "04d621d9bfc1617fab3aaa23fd2c2e4eaf099be8"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install:append() {
    install -d ${D}${WEBOS_APPLICATION_TARGET_DIR}
    install -v -m 0644 ${S}/receiver.html ${D}${WEBOS_APPLICATION_TARGET_DIR}/
}
