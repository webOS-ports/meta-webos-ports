# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "Enyo 1.0 Browser application"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# corresponds to tag 0.21
SRCREV = "0f2a339d30023018d1d0d435b2aa0350a32c83e4"
PV = "4.0.0-0.21"

inherit webos_public_repo
inherit webos_submissions
inherit webos_arch_indep

WEBOS_GIT_TAG = "${WEBOS_SUBMISSION}"
WEBOS_REPO_NAME = "isis-browser"
SRC_URI = "${ISIS_PROJECT_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
WEBOS_PORTS_REPO_NAME = "isis-browser"
SRCREV = "57f91972e6d1abc8c32e476920e1683d026b4d3e"

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
