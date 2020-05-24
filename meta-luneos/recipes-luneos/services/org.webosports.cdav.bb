SUMMARY = "Node.js service to provide synergy connector for CardDav and CalDav"
SECTION = "webos/services"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d32239bcb673463ab874e80d47fae504"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus
inherit webos_app

PV = "0.3.34+git${SRCPV}"
SRCREV = "f5eef62268edd2abf24a30169b7e9de3da910046"

SERVICE_NAME = "org.webosports.service.cdav"
WEBOS_REPO_NAME = "org.webosports.service.contacts.carddav"
WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

CLEANBROKEN = "1"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

do_install_append() {
    # the service itself
    install -d ${D}${webos_servicesdir}/org.webosports.service.cdav
    cp -rv ${S}/service/* ${D}${webos_servicesdir}/org.webosports.service.cdav
    rm -rf ${D}${webos_servicesdir}/org.webosports.service.cdav/configuration
    
    # ACG configuration files
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/${SERVICE_NAME}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/${SERVICE_NAME}.api.json
    
    # db8 kinds and permissions
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -vrf ${S}/service/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds/
    cp -vrf ${S}/service/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions/

    # account templates
    install -d ${D}${webos_accttemplatesdir}/org.webosports.cdav
    cp -vrf ${S}/accounts-enyo/* ${D}${webos_accttemplatesdir}/org.webosports.cdav

    install -d ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.google
    cp -vrf ${S}/accounts-google/* ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.google

    install -d ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.icloud
    cp -vrf ${S}/accounts-icloud/* ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.icloud

    install -d ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.yahoo
    cp -vrf ${S}/accounts-yahoo/* ${D}${webos_accttemplatesdir}/org.webosports.cdav.account.yahoo

    # account creation application
    install -d ${D}${webos_applicationsdir}/org.webosports.app.cdav
    cp -rv ${S}/app-enyo/* ${D}${webos_applicationsdir}/org.webosports.app.cdav/

    # copy urlschemes.js from service dir to application dir
    cp -v ${S}/service/javascript/urlschemes.js ${D}${webos_applicationsdir}/org.webosports.app.cdav/CrossAppTarget/
}

FILES_${PN} += "${webos_applicationsdir} ${webos_servicesdir} ${webos_accttemplatesdir}"
