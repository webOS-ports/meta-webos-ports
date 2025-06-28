# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Loadable foundation frameworks for apps and services"
SECTION = "webos/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.1-3+git"
SRCREV = "2444cbf9c43c8485ba6b44c91ab3c2d6fcf9680c"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit allarch

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {
    install -d ${D}${webos_frameworksdir}

    for FRAMEWORK in `ls -d1 ${S}/foundations*` ; do
        FRAMEWORK_DIR=`basename $FRAMEWORK`
        install -d ${D}${webos_frameworksdir}/$FRAMEWORK_DIR/version/1.0/
        cp -vrf $FRAMEWORK/* ${D}${webos_frameworksdir}/$FRAMEWORK_DIR/version/1.0/
        # remove test and jasminetest dirs
        rm -vrf ${D}${webos_frameworksdir}/$FRAMEWORK_DIR/version/1.0/*test
    done
}

FILES:${PN} += "${webos_frameworksdir}"
