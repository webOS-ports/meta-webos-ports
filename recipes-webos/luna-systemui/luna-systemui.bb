# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "WebOS user interface support module"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# corresponds to tag submissions/1.02
SRCREV = "9db5f65a072d2f5af7070efbf38af41705232990"
PV = "2.0.0-1.02"

inherit webos_enhanced_submissions
inherit webos_arch_indep
inherit webos_machine_dep
inherit webos_public_repo

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

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
