SUMMARY = "webOS Ports Messaging service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING-Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit webos_ports_repo
inherit allarch
inherit webos_system_bus
inherit webos_filesystem_paths

PV = "0.1.0+git${SRCPV}"
SRCREV = "dbc75b435ec060bb8bb5b8bd71d399bb23a8be15"

WEBOS_REPO_NAME = "org.webosports.messaging"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git/service"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

do_install() {
    # Install service files
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}

    install -d ${D}${webos_sysconfdir}/db/kinds
    install -m 0644 ${S}/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds

    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions

    install -d ${D}${webos_sysconfdir}/activities
    cp -rv ${S}/configuration/activities/org.webosports.service.messaging ${D}${webos_sysconfdir}/activities/

    # Remove things we don't want on the device
    rm -rf ${D}${webos_servicesdir}/${PN}/files
}

FILES_${PN} += "${webos_servicesdir}/${PN}"
