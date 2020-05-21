# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Just Type application for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "4.0.0-2+git${SRCPV}"
SRCREV = "33e1148969c265c076025187f29dc9e541b7bf29"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit allarch
inherit webos_app

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${webos_applicationsdir}/com.palm.launcher
    cp -vrf ${S}/* ${D}${webos_applicationsdir}/com.palm.launcher
}

FILES_${PN} += "${webos_applicationsdir}/com.palm.launcher"
