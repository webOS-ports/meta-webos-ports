SUMMARY = "Node.js service to ask for permission to media indexer database."
SECTION = "webos/services"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d32239bcb673463ab874e80d47fae504"

inherit webos_ports_repo
inherit webos_system_bus
inherit allarch
inherit webos_filesystem_paths

PV = "0.1.0+git"
PR = "r1"
SRCREV = "f19c88723ae1e8ee11b5a487b29787e4d32d6234"
SERVICE_NAME = "com.palm.mediapermissions"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# The indexer grants what the user allows (grantRead); the bus module tells
# the service which program sent an answer (Message.senderExePath()).
RDEPENDS:${PN} += "mediaindexer nodejs-module-webos-sysbus"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

do_install:append() {

    # ACG configuration files
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.service
    # the service's own, and those letting every app ask and the shell answer
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/*.perm.json ${D}${webos_sysbus_permissionsdir}/
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/${SERVICE_NAME}.api.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.groups.json ${D}${webos_sysbus_groupsdir}/${SERVICE_NAME}.groups.json
    
    # db8 kinds and permissions
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -vrf ${S}/service/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds/
    cp -vrf ${S}/service/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions/

    # The service itself, which run-js-service starts from here
    install -d ${D}${webos_servicesdir}/${SERVICE_NAME}
    install -v -m 0644 ${S}/service/services.json ${S}/service/sources.json ${D}${webos_servicesdir}/${SERVICE_NAME}/
    cp -vr ${S}/service/javascript ${D}${webos_servicesdir}/${SERVICE_NAME}/
}

FILES:${PN} += "${webos_servicesdir}"
