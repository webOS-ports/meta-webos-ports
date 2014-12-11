# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Core applications that are part of Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "3.0.0-2_df161a4f0c8374ab8a33a295698154a612ceaf08"

#inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
#inherit webos_cmake
inherit allarch

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "69850dd71efc981ba3f1d0a786f3c9a958b1c3e5"

do_install() {
    # WEBOS_INSTALL_WEBOS_COREAPPSDIR
    install -d ${D}${webos_applicationsdir}
    #INSTALL DB/KINDS
    install -d ${D}${webos_sysconfdir}/db/kinds
    #INSTALL DB/PERSMISSIONS
    install -d ${D}${webos_sysconfdir}/db/permissions
    #INSTALL ACTIVITIES
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
    # Drop the notes application, we ship our own
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.notes
    rm -rf ${D}${sysconfdir}/palm/db/permissions/com.palm.note
    rm -rf ${D}${sysconfdir}/palm/db/kinds/com.palm.note
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.calculator
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.clock
    # NOTE we have to drop the contacts and calendar app here as we still need their db8
    # configurations
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.contacts
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.calendar
}

FILES_${PN} += "${webos_applicationsdir} ${webos_sysconfdir}"
