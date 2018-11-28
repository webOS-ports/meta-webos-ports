# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "JavaScript services for apps"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "3.0.1-5+git${SRCPV}"
SRCREV = "27af277849fc55de176997a4e09211ed49122b7e"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_configure_manifest
inherit allarch

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
    install -d ${D}${webos_sysbus_pubservicesdir}
    install -d ${D}${webos_sysbus_prvservicesdir}
    install -d ${D}${webos_sysbus_dynpubrolesdir}
    install -d ${D}${webos_sysbus_dynprvrolesdir}
    install -d ${D}${webos_sysbus_dynpubservicesdir}
    install -d ${D}${webos_sysbus_dynprvservicesdir}
    install -d ${D}${webos_sysbus_devpubservicesdir}
    install -d ${D}${webos_sysbus_devprvservicesdir}
    install -d ${D}${webos_sysbus_devpubrolesdir}
    install -d ${D}${webos_sysbus_devprvrolesdir}
    install -d ${D}${webos_sysbus_prvrolesdir}
    install -d ${D}${webos_sysbus_pubrolesdir}
    install -d ${D}${webos_localstatedir}/data/com.palm.appInstallService
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_permissionsdir}

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
        cp -vrf $SERVICE/files/sysbus/*.role.json ${D}${webos_sysbus_prvrolesdir} 2> /dev/null || true
        cp -vrf $SERVICE/files/sysbus/*.role.json ${D}${webos_sysbus_pubrolesdir} 2> /dev/null || true
        cp -vrf $SERVICE/files/sysbus/*.service ${D}${webos_sysbus_prvservicesdir} 2> /dev/null || true
        cp -vrf $SERVICE/files/sysbus/*.api.json ${D}${webos_sysbus_apipermissionsdir} 2> /dev/null || true
        cp -vrf $SERVICE/files/sysbus/*.perm.json ${D}${webos_sysbus_permissionsdir} 2> /dev/null || true
    done

# install account services files in public service directory.
    cp -vrf ${S}/com.palm.service.accounts/files/sysbus/*.service ${D}${webos_sysbus_pubservicesdir} 2> /dev/null || true

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

# install account service upstart files
    install -d ${D}${sysconfdir}/event.d 2> /dev/null || true
    install -m 644 ${S}/com.palm.service.accounts/files/etc/event.d/createLocalAccount ${D}${sysconfdir}/event.d/
    install -d ${D}${sysconfdir}/init 2> /dev/null || true
    install -m 644 ${S}/com.palm.service.accounts/files/etc/init/createLocalAccount.conf ${D}${sysconfdir}/init/

# create folder for contact linker plugins
    mkdir -p ${D}${sysconfdir}/palm/contact_linker_plugins
}

FILES_${PN} += "${webos_servicesdir} ${webos_sysconfdir} ${sysconfdir}"
FILES_${PN} += "${webos_sysbus_pubservicesdir} ${webos_sysbus_prvservicesdir}"
FILES_${PN} += "${webos_sysbus_prvrolesdir} ${webos_sysbus_pubrolesdir}"
FILES_${PN} += "${webos_accttemplatesdir} ${webos_sysbus_dynpubrolesdir}"
FILES_${PN} += "${webos_sysbus_dynprvrolesdir} ${webos_sysbus_devpubservicesdir}"
FILES_${PN} += "${webos_sysbus_devprvservicesdir} ${webos_sysbus_devpubrolesdir}"
FILES_${PN} += "${webos_sysbus_devprvrolesdir} ${webos_sysbus_dynpubservicesdir}"
FILES_${PN} += "${webos_sysbus_dynprvservicesdir}"
FILES_${PN} += "${webos_sysbus_manifestsdir} ${webos_sysbus_apipermissionsdir} ${webos_sysbus_permissionsdir}"
