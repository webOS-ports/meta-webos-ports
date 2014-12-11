# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Enyo 1.0 Browser application"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# isis-project components don't have submissions
PE = "1"
PV = "0.21+wop+gitr${SRCPV}"
SRCREV = "57f91972e6d1abc8c32e476920e1683d026b4d3e"

inherit webos_public_repo
inherit allarch

WEBOS_REPO_NAME = "isis-browser"
SRC_URI = "${ISIS_PROJECT_GIT_REPO_COMPLETE}"

inherit webos-ports-repo
S = "${WORKDIR}/git"

do_install() {
    # WEBOS_INSTALL_WEBOS_COREAPPSDIR
    install -d ${D}${webos_applicationsdir}/${PN}
    #INSTALL DB/KINDS
    install -d ${D}${webos_sysconfdir}/db/kinds
    #INSTALL DB/PERSMISSIONS
    install -d ${D}${webos_sysconfdir}/db/permissions

    cp -vrf ${S}/* ${D}${webos_applicationsdir}/${PN}

    if [ -d db/kinds ]; then
        install -v -m 644 db/kinds/* ${D}${webos_sysconfdir}/db/kinds
    fi
    rm -vrf ${D}${webos_applicationsdir}/${PN}/db/kinds

    if [ -d db/permissions ]; then
        install -v -m 644 db/permissions/* ${D}${webos_sysconfdir}/db/permissions
    fi
    rm -vrf ${D}${webos_applicationsdir}/${PN}/db/permissions
}

FILES_${PN} += "${webos_applicationsdir}"
