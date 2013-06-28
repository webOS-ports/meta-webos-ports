# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "Initialization and font setup files used by luna-sysmgr"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# corresponds to tag submissions/1.03
SRCREV = "06f54c3315387107c2af340409464895c724cd5c"
PV = "2.0.0-1.03"

#inherit webos_component TODO
inherit webos_arch_indep
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    # Expand fonts tarball
    if [ -e ${S}/files/conf/fonts/fonts.tgz ]; then
        install -d ${D}${datadir}/fonts
        tar xvzf ${S}/files/conf/fonts/fonts.tgz --directory=${D}${datadir}/fonts
    fi
}

FILES_${PN} += "${webos_prefix} ${webos_sysconfdir} ${datadir}/fonts"
