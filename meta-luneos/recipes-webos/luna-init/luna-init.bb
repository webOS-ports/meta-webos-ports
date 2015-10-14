# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Initialization, setup, and font files used by luna-sysmgr and luna-sysservice"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "tzdata python-tz-native"

PV = "2.0.1-10+git${SRCPV}"
SRCREV = "5c43c32827be1b3373a96705e7b28d1566d99bf8"

inherit allarch
inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pythonnative

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
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
    install -v -m 644 ${S}/src/mccInfo.json ${D}${webos_sysconfdir}
}

PACKAGES =+ "${PN}-fonts"
FILES_${PN} += "${webos_prefix} ${webos_sysconfdir}"
FILES_${PN}-fonts += "${libdir}/fonts/"
