# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "WebOS user interface support module"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PE = "1"
PV = "2.0.1-2+git"
SRCREV = "8de192dc00f4bf58e50b54750269bf71365279f5"
PR = "r2"

inherit allarch
inherit webos_filesystem_paths
inherit webos_ports_fork_repo
inherit webos_app

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {
    install -d ${D}${webos_applicationsdir}/com.palm.systemui
    cp -vrf ${S}/* ${D}${webos_applicationsdir}/com.palm.systemui/
}

FILES:${PN} += "${webos_applicationsdir}/com.palm.systemui"
