# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Open webOS Just Type daemon"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libxml2 luna-service2 glib-2.0 cjson sqlite3 "

WEBOS_VERSION = "2.0.0-1_718edcb850c05fe61d50e1fb2c2272696cca171b"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_system_bus
inherit webos_daemon

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# TODO: Remove once there's localization support
do_install_append() {
    install -d ${D}${webos_prefix}/universalsearchmgr/resources/en_us
    install -v -m 0644 ${S}/files/UniversalSearchList.json ${D}${webos_prefix}/universalsearchmgr/resources/en_us
}

FILES_${PN} += "${webos_prefix}"

inherit webos-ports-submissions
SRCREV = "e276e787c5042492e885a6f9f84e824de90abc18"
