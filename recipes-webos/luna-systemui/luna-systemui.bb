# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "WebOS user interface support module"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "2.0.0-1.02_e525675c52066682c491e0e928cce7be5cab39e0"

inherit webos_enhanced_submissions
inherit webos_arch_indep
inherit webos_machine_dep
inherit webos_public_repo

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "40a40f5db63b53b7ed157326729f809db062480f"

do_install() {
    #COPY ENTIRE APP
    install -d ${D}${webos_sysmgr_datadir}/system/luna-systemui
    cp -vrf ${S}/* ${D}${webos_sysmgr_datadir}/system/luna-systemui
    if [ -e ${S}/images/wallpaper.tar ]; then
        install -d ${D}${webos_sysmgr_datadir}/system/luna-systemui/images
        tar xvf ${S}/images/wallpaper.tar --directory=${D}${webos_sysmgr_datadir}/system/luna-systemui/images
    fi
}

FILES_${PN} += "${webos_sysmgr_datadir}/system"
