# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "JavaScript services for apps"
SECTION = "webos/services"
# The contacts, contacts.linker, accounts and calendar.reminders services are
# GPL-3.0-or-later, each with its own COPYING; the files kept from the old
# services stay Apache-2.0, DateJS (calendar.reminders) and the JSON schema
# validator (accounts) MIT.
LICENSE = "Apache-2.0 & GPL-3.0-or-later & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://com.palm.service.contacts.linker/COPYING;md5=1ebbd3e34237af26da5dc08a4e440464 \
                    file://com.palm.service.contacts/COPYING;md5=1ebbd3e34237af26da5dc08a4e440464 \
                    file://com.palm.service.accounts/COPYING;md5=1ebbd3e34237af26da5dc08a4e440464 \
                    file://com.palm.service.calendar.reminders/COPYING;md5=1ebbd3e34237af26da5dc08a4e440464 \
                    file://com.palm.service.calendar.reminders/NOTICE;md5=3d5ed1f34a3903962a2a026c72e86f28 \
"

PV = "3.0.1-5+git"
SRCREV = "0275f7495a809d869afd9f88256656c45cc1d968"
# 0275f749 is on herrie/all-node-services: webOS-ports/webOS-OSE 3e60c21 with app-services PRs #29-#33 merged
# (contacts.linker, accounts, getAccountInfo, calendar.reminders, contacts); back to webOS-ports/webOS-OSE once they are.
WEBOS_GIT_PARAM_BRANCH = "herrie/all-node-services"

inherit webos_ports_ose_repo
inherit webos_filesystem_paths
inherit webos_system_bus
inherit allarch

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {
    install -d ${D}${webos_servicesdir}
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    install -d ${D}${webos_sysconfdir}/db_kinds
    install -d ${D}${webos_sysconfdir}/mediadb/kinds
    install -d ${D}${webos_sysconfdir}/mediadb/permissions
    install -d ${D}${webos_sysconfdir}/activities
    install -d ${D}${webos_sysconfdir}/filecache_types
    install -d ${D}${webos_localstatedir}/data/com.palm.appInstallService
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_groupsdir}

    for SERVICE in `ls -d1 ${S}/com.palm.service.*` ; do
        SERVICE_DIR=`basename $SERVICE`
        install -d ${D}${webos_servicesdir}/$SERVICE_DIR/
        cp -rf $SERVICE/* ${D}${webos_servicesdir}/$SERVICE_DIR/
        # Copy db8 kinds, permissions and activities
        cp -vrf $SERVICE/db/kinds/* ${D}${webos_sysconfdir}/db/kinds/ 2> /dev/null || true
        cp -vrf $SERVICE/db/permissions/* ${D}${webos_sysconfdir}/db/permissions/ 2> /dev/null || true
        cp -vrf $SERVICE/activities/* ${D}${webos_sysconfdir}/activities/ 2> /dev/null || true
        cp -vrf $SERVICE/filecache_types/* ${D}${webos_sysconfdir}/filecache_types/ 2> /dev/null || true
        # Copy services and roles files
        cp -vrf $SERVICE/files/sysbus/*.api.json ${D}${webos_sysbus_apipermissionsdir} 2> /dev/null || true
        cp -vrf $SERVICE/files/sysbus/*.perm.json ${D}${webos_sysbus_permissionsdir} 2> /dev/null || true
        cp -vrf $SERVICE/files/sysbus/*.role.json ${D}${webos_sysbus_rolesdir} 2> /dev/null || true
        cp -vrf $SERVICE/files/sysbus/*.service ${D}${webos_sysbus_servicedir} 2> /dev/null || true
        cp -vrf $SERVICE/files/sysbus/*.groups.json ${D}${webos_sysbus_groupsdir} 2> /dev/null || true
    done

# install account templates.
    install -d ${D}${webos_accttemplatesdir} 2> /dev/null || true
    cp -vrf ${S}/account-templates/palmprofile/com.palm.palmprofile ${D}${webos_accttemplatesdir}
    cp -vrf ${S}/account-templates/sim/com.palm.sim ${D}${webos_accttemplatesdir}

# install temp db kinds and permissions
    install -d ${D}${webos_sysconfdir}/tempdb/kinds 2> /dev/null || true
    install -d ${D}${webos_sysconfdir}/tempdb/permissions 2> /dev/null || true
    cp -vrf com.palm.service.accounts/tempdb/kinds/* ${D}${webos_sysconfdir}/tempdb/kinds/ 2> /dev/null || true
    cp -vrf com.palm.service.accounts/tempdb/permissions/* ${D}${webos_sysconfdir}/tempdb/permissions/ 2> /dev/null || true

# create folder for contact linker plugins
    mkdir -p ${D}${sysconfdir}/palm/contact_linker_plugins
}

FILES:${PN} += "${webos_servicesdir} ${webos_sysconfdir} ${sysconfdir}"
FILES:${PN} += "${webos_accttemplatesdir}"
FILES:${PN} += "${webos_sysbus_apipermissionsdir} ${webos_sysbus_manifestsdir} ${webos_sysbus_permissionsdir} ${webos_sysbus_rolesdir} ${webos_sysbus_servicedir} ${webos_sysbus_groupsdir}"
