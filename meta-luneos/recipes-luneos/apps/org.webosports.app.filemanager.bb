SUMMARY = "File manager application for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_system_bus
inherit webos_filesystem_paths
inherit webos_app

SERVICE_NAME = "org.webosports.service.filemanager"

PV = "1.0.0+git${SRCPV}"
SRCREV = "2d234b2fe864cbb723e5b9a2376eee92a710c83f"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service/sysbus"

do_install_append() {
    install -d ${D}${webos_servicesdir}/org.webosports.service.filemanager
    cp -r ${S}/service/* ${D}${webos_servicesdir}/org.webosports.service.filemanager/
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/${SERVICE_NAME}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/${SERVICE_NAME}.api.json
}

FILES_${PN} += "${webos_servicesdir}/${SERVICE_NAME}"
