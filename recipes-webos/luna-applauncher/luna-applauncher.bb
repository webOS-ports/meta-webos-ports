# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Just Type application for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "4.0.0-2_56cd14b8287a7db7a9a8b4310cc298260e5c545d"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_arch_indep

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "5e4079b7d6db3254679fa6a0a38d5a5db32d41a9"

do_install() {
    install -d ${D}${webos_applicationsdir}/com.palm.launcher
    cp -vrf ${S}/* ${D}${webos_applicationsdir}/com.palm.launcher
}

FILES_${PN} += "${webos_applicationsdir}/com.palm.launcher"
