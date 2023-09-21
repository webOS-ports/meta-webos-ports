# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Enyo 1.0 JavaScript application framework"
SECTION = "webos/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0-128.2+git${SRCPV}"
SRCREV = "02502917b3ea60add7f6a08aa706dd00bc232c5b"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit allarch

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/wam-sam"

do_install() {
    install -d ${D}${webos_frameworksdir}/enyo/0.10/framework
    cp -vrf ${S}/framework/* ${D}${webos_frameworksdir}/enyo/0.10/framework

    # Create symlink for enyo/1.0 (points to enyo/0.10)
    ln -vs 0.10 ${D}${webos_frameworksdir}/enyo/1.0
    # Create symlink for tellurium (so the inspector doesn't give errors)
    ln -vs enyo/0.10/framework/build/palm/tellurium ${D}${webos_frameworksdir}/tellurium
}

FILES:${PN} += "${webos_frameworksdir}"
