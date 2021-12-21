SUMMARY = "webOS Ports Messaging service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING-Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit webos_ports_repo
inherit allarch
inherit webos_system_bus
inherit webos_filesystem_paths

PV = "0.1.0+git${SRCPV}"
SRCREV = "838e1a4e0cb25908cfb7a6c6d333322842d4a0e7"

WEBOS_REPO_NAME = "org.webosports.messaging"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git/service"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${WORKDIR}/git/files/sysbus"
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

do_install() {
    # Install service files
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}

    install -d ${D}${webos_sysconfdir}/db/kinds
    install -m 0644 ${S}/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds

    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions

    install -d ${D}${webos_sysconfdir}/activities
    cp -rv ${S}/configuration/activities/org.webosports.service.messaging ${D}${webos_sysconfdir}/activities/

    # Install the ACG configuration
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service ${D}${webos_sysbus_servicedir}/${BPN}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.role.json ${D}${webos_sysbus_rolesdir}/${BPN}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.api.json ${D}${webos_sysbus_apipermissionsdir}/${BPN}.api.json
    
    # Remove things we don't want on the device
    rm -rf ${D}${webos_servicesdir}/${PN}/files
}

FILES:${PN} += "${webos_servicesdir}/${PN}"
