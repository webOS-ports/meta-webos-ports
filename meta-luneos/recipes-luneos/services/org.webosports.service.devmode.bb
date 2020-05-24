SUMMARY = "webOS Ports Developer mode management service"
SECTION = "webos/services"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "0.1.0+git${SRCPV}"
SRCREV = "522c54e846be110ca27217136388404fa4d885ff"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
WEBOS_GIT_PARAM_BRANCH = "herrie/acg"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

do_install() {
    # Install service and remove unecessary things
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}
    rm -rf ${D}${webos_servicesdir}/${PN}/files
    
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

FILES_${PN} += "${webos_servicesdir}/${PN}"
