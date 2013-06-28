# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "Just Type application for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# corresponds to tag submissions/1.00
SRCREV = "1ac4108ff35e96603bf3fe855cc0a6d8d01f4c89"
PV = "3.0.0-1.00"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_arch_indep
inherit webos_machine_dep

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "e497bc1e349dbe73f56eb0e95c8da8c1f7be2b28"

do_install() {
    #COPY ENTIRE APP
    install -d ${D}${webos_sysmgr_datadir}/system/luna-applauncher
    cp -vrf ${S}/* ${D}${webos_sysmgr_datadir}/system/luna-applauncher
}

FILES_${PN} += "${webos_sysmgr_datadir}/system"
