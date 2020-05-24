SUMMARY = "License management service for the webOS ports project"
SECTION = "webos/services"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_repo
inherit webos_system_bus
inherit allarch

PV = "0.1.0+git${SRCPV}"
SRCREV = "9c489cb8d2d416d849a2bbcbabcb62b69869b995"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"
PALM_DIR = "${prefix}/palm"

do_install() {
    install -d ${D}${PALM_DIR}/services

    SERVICE_DIR="${PN}"
    install -d ${D}${PALM_DIR}/services/$SERVICE_DIR/
    cp -vf ${S}/*.js* ${D}${PALM_DIR}/services/$SERVICE_DIR/
    
    # Install the ACG configuration
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service ${D}${webos_sysbus_servicedir}/${BPN}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.role.json ${D}${webos_sysbus_rolesdir}/${BPN}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.api.json ${D}${webos_sysbus_apipermissionsdir}/${BPN}.api.json
}

FILES_${PN} += "${PALM_DIR}/services"
