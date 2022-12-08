SUMMARY = "webOS Ports system update service"
SECTION = "webos/services"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_ports_ose_repo
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "0.1.0+git${SRCPV}"
SRCREV = "8cee63b4af35f759db0529b5daab2229d8a6904a"

WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg"
WEBOS_REPO_NAME = "org.webosports.update"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git/service"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${WORKDIR}/git/files/sysbus"

do_install() {
    # Install service and remove unecessary things
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}
    rm -rf ${D}${webos_servicesdir}/${PN}/activities
    rm -rf ${D}${webos_servicesdir}/${PN}/files

    install -d ${D}${webos_sysconfdir}/activities
    cp -rv ${S}/activities/org.webosports.service.update ${D}${webos_sysconfdir}/activities/

    chmod +x ${D}${webos_servicesdir}/${PN}/start-update.sh
    chmod +x ${D}${webos_servicesdir}/${PN}/download-updates.sh
     
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

FILES:${PN} += "${webos_servicesdir}/${PN}"
RDEPENDS:${PN} = "bash"
