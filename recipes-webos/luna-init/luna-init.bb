# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Initialization, setup, and font files used by luna-sysmgr and luna-sysservice"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "2.0.1-6_1b94f1a3e903880db97287a63eb8e572e3eb42c3"

#inherit webos_component TODO
inherit webos_arch_indep
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    # Expand fonts tarball
    if [ -e ${S}/files/conf/fonts/fonts.tgz ]; then
        install -d ${D}${libdir}/fonts
        tar xvzf ${S}/files/conf/fonts/fonts.tgz --directory=${D}${libdir}/fonts
    fi
    # Remove fonts installed into the wrong location
    rm -rf ${D}${datadir}/fonts

    install -d ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/locale.txt ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/defaultPreferences.txt ${D}${webos_sysconfdir}
}

PACKAGES =+ "${PN}-fonts"
FILES_${PN} += "${webos_prefix} ${webos_sysconfdir}"
FILES_${PN}-fonts += "${libdir}/fonts/"
