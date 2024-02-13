# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "WebOS user interface support module"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "2.0.1-2+git"
SRCREV = "21c501b1d3533d67589fb1a02434ee519d23aad6"

inherit allarch
inherit webos_filesystem_paths
inherit webos_ports_fork_repo
inherit webos_app

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${webos_applicationsdir}/com.palm.systemui
    cp -vrf ${S}/* ${D}${webos_applicationsdir}/com.palm.systemui/
}

FILES:${PN} += "${webos_applicationsdir}/com.palm.systemui"
