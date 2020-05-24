# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "JavaScript services for apps"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "3.0.1-5+git${SRCPV}"
SRCREV = "ea40249579544904b20ad74d7ffdcdf31aa2d750"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_configure_manifest
inherit allarch

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

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
    done

# install account service desktop credentials db kind 
    cp -vrf ${S}/com.palm.service.accounts/desktop/com.palm.account.credentails ${D}${webos_sysconfdir}/db/kinds 2> /dev/null || true

# install account templates.
    install -d ${D}${webos_accttemplatesdir} 2> /dev/null || true
    cp -vrf ${S}/account-templates/palmprofile/com.palm.palmprofile ${D}${webos_accttemplatesdir}

# install temp db kinds and permissions
    install -d ${D}${webos_sysconfdir}/tempdb/kinds 2> /dev/null || true
    install -d ${D}${webos_sysconfdir}/tempdb/permissions 2> /dev/null || true
    cp -vrf com.palm.service.accounts/tempdb/kinds/* ${D}${webos_sysconfdir}/tempdb/kinds/ 2> /dev/null || true
    cp -vrf com.palm.service.accounts/tempdb/permissions/* ${D}${webos_sysconfdir}/tempdb/permissions/ 2> /dev/null || true

# create folder for contact linker plugins
    mkdir -p ${D}${sysconfdir}/palm/contact_linker_plugins
}

FILES_${PN} += "${webos_servicesdir} ${webos_sysconfdir} ${sysconfdir}"
FILES_${PN} += "${webos_accttemplatesdir}"
FILES_${PN} += "${webos_sysbus_apipermissionsdir} ${webos_sysbus_manifestsdir} ${webos_sysbus_permissionsdir} ${webos_sysbus_rolesdir} ${webos_sysbus_servicedir}"
