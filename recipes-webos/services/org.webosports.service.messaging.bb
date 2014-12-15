SUMMARY = "webOS Ports Messaging service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING-Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit webos_ports_repo
inherit allarch
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"
SRCREV = "796dc9fce03bd6e9c7276d92a3487523336c4780"

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
    install -m 0644 ${S}/files/db8/kinds/* ${D}${webos_sysconfdir}/db/kinds

    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/files/db8/permissions/* ${D}${webos_sysconfdir}/db/permissions

    # Remove things we don't want on the device
    rm -rf ${D}${webos_servicesdir}/${PN}/files
}

FILES_${PN} += "${webos_servicesdir}/${PN}"
