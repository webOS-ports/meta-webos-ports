SUMMARY = "Tweaks for your webOS device."
SECTION = "webos/apps"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit webos_filesystem_paths
inherit webos_ports_repo
inherit allarch
inherit webos_system_bus

PV = "3.0.3+git${SRCPV}"
SRCREV = "8cbbc646cb000813e38e4b77b83a82437d5475c8"

SERVICE_NAME = "org.webosports.service.tweaks.prefs"
APP_NAME = "org.webosports.app.tweaks"
WEBOS_REPO_NAME = "tweaks"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"


do_compile() {
}

do_install_append() {
    install -d ${D}${webos_applicationsdir}/${APP_NAME}
    cp -rv ${S}/enyo-app/* ${D}${webos_applicationsdir}/${APP_NAME}

    install -d ${D}${webos_servicesdir}/${SERVICE_NAME}
    cp -rv ${S}/node-service/* ${D}${webos_servicesdir}/${SERVICE_NAME}
    cp -rv ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_servicesdir}/${SERVICE_NAME}

    # Install the ACG configuration
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/${SERVICE_NAME}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/${SERVICE_NAME}.api.json

    # Install the db8 configuration & permissions
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -m 0644 ${S}/node-service/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds

    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/node-service/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions
}

PACKAGES = "${PN}"
FILES_${PN} = " \
    ${webos_applicationsdir}/org.webosports.app.tweaks \
    ${webos_servicesdir}/org.webosports.service.tweaks.prefs \
    ${webos_sysconfdir} \
    ${webos_sysbus_manifestsdir} ${webos_sysbus_apipermissionsdir} ${webos_sysbus_permissionsdir} ${webos_sysbus_rolesdir} ${webos_sysbus_servicedir} \
"
