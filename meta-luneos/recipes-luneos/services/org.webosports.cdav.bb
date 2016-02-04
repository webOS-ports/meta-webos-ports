SUMMARY = "Node.js service to provide synergy connector for CardDav and CalDav"
SECTION = "webos/services"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d32239bcb673463ab874e80d47fae504"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "0.3.33+gitr${SRCPV}"
SRCREV = "9215dec870060c4646c37890ea9ba88dbf50e695"

WEBOS_REPO_NAME = "org.webosports.service.contacts.carddav"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service/sysbus"

do_install() {
    # the service itself
    install -d ${D}${webos_servicesdir}/org.webosports.cdav.service
    cp -rv ${S}/service/* ${D}${webos_servicesdir}/org.webosports.cdav.service
    rm -rf ${D}${webos_servicesdir}/org.webosports.cdav.service/configuration

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
    install -d ${D}${webos_applicationsdir}/org.webosports.cdav.app
    cp -rv ${S}/app-enyo/* ${D}${webos_applicationsdir}/org.webosports.cdav.app/

    # copy urlschemes.js from service dir to application dir
    cp -v ${S}/service/javascript/urlschemes.js ${D}${webos_applicationsdir}/org.webosports.cdav.app/CrossAppTarget/
}

FILES_${PN} += "${webos_applicationsdir} ${webos_servicesdir} ${webos_accttemplatesdir}"
