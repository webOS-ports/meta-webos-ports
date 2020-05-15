# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Core applications that are part of Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "3.0.0-2+git${SRCPV}"
SRCREV = "aedb113ac3d73676802208d99e70a12b428f494e"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_app

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install() {
    # WEBOS_INSTALL_WEBOS_COREAPPSDIR
    install -d ${D}${webos_applicationsdir}
    # Install db8 kinds
    install -d ${D}${webos_sysconfdir}/db/kinds
    # Install db8 permissions
    install -d ${D}${webos_sysconfdir}/db/permissions
    # Install activities
    install -d ${D}${webos_sysconfdir}/activities

    for COREAPPS in `ls -d1 ${S}/com.palm.app*` ; do
        COREAPPS_DIR=`basename $COREAPPS`
        install -d ${D}${webos_applicationsdir}/$COREAPPS_DIR/
        cp -vrf $COREAPPS/* ${D}${webos_applicationsdir}/$COREAPPS_DIR/

        if [ -d $COREAPPS/configuration/db/kinds ]; then
            install -v -m 644 $COREAPPS/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds
        fi

        if [ -d $COREAPPS/configuration/db/permissions ]; then
            install -v -m 644 $COREAPPS/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions
        fi

        if [ -d $COREAPPS/configuration/activities ]; then
            cp -vrf $COREAPPS/configuration/activities/* ${D}${webos_sysconfdir}/activities/
        fi
    done
    # Drop the Contacts, Notes & Calculator applications, we ship our own Enyo 2 variant
    # We keep all the db kinds & permissions because other apps can use these too.
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.notes
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.calculator
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.contacts
}

FILES_${PN} += "${webos_applicationsdir} ${webos_sysconfdir}"

RDEPENDS_${PN} = "bash"
