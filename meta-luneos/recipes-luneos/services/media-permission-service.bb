SUMMARY = "Node.js service to ask for permission to media indexer database."
SECTION = "webos/services"
# The Node 22 rewrite is GPL-3.0-or-later (LICENSE.txt); what it takes from
# mojoservice and Foundations stays Apache-2.0 (LICENSE.Apache-2.0).
LICENSE = "GPL-3.0-or-later & Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=1ebbd3e34237af26da5dc08a4e440464 \
                    file://LICENSE.Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57 \
"

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
    install -v -m 0644 ${S}/service/package.json ${S}/service/service.js ${D}${webos_servicesdir}/${SERVICE_NAME}/
    cp -vr ${S}/service/lib ${D}${webos_servicesdir}/${SERVICE_NAME}/
}

FILES:${PN} += "${webos_servicesdir}"
