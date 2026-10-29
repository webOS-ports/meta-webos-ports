SUMMARY = "License management service for the webOS ports project"
SECTION = "webos/services"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

inherit webos_ports_repo
inherit webos_system_bus
inherit allarch

PV = "0.1.0+git"
SRCREV = "2cfb8eeb2f457b073bb411afdcc9a59d2d7dc111"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"
PALM_DIR = "${prefix}/palm"

do_install() {
    install -d ${D}${PALM_DIR}/services

    SERVICE_DIR="${PN}"
    install -d ${D}${PALM_DIR}/services/$SERVICE_DIR/
    # The Node 22 service, which run-js-service starts from its package.json
    cp -vf ${S}/package.json ${S}/service.js ${D}${PALM_DIR}/services/$SERVICE_DIR/
    cp -vr ${S}/lib ${D}${PALM_DIR}/services/$SERVICE_DIR/
    
    # Install the ACG configuration
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service ${D}${webos_sysbus_servicedir}/${BPN}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.role.json ${D}${webos_sysbus_rolesdir}/${BPN}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.api.json ${D}${webos_sysbus_apipermissionsdir}/${BPN}.api.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.groups.json ${D}${webos_sysbus_groupsdir}/${BPN}.groups.json
}

FILES:${PN} += "${PALM_DIR}/services"
