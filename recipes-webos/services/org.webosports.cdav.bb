SUMMARY = "Node.js service to provide synergy connector for CardDav and CalDav"
SECTION = "webos/services"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d32239bcb673463ab874e80d47fae504"

inherit webos_public_repo
inherit webos_arch_indep

PV = "0.2.3+gitr${SRCPV}"

SRCREV = "015da22828a2eea884ec5718c9071df589eb6f4d"
SRC_URI = "git://github.com/webOS-ports/org.webosports.service.contacts.carddav;protocol=git;branch=master"
S = "${WORKDIR}/git"

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

    # account creation application
    install -d ${D}${webos_applicationsdir}/org.webosports.cdav.app
    cp -rv ${S}/app-enyo/* ${D}${webos_applicationsdir}/org.webosports.cdav.app/
}

FILES_${PN} += "${webos_applicationsdir} ${webos_servicesdir} ${webos_accttemplatesdir}"
