SUMMARY = "Node.js service to ask for permission to media indexer database."
SECTION = "webos/services"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d32239bcb673463ab874e80d47fae504"

inherit webos_ports_repo
inherit allarch
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"
SRCREV = "d7e0870414466c390ce04c46f6315c36db494285"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service/sysbus"

do_install() {
    # the service itself
    install -d ${D}${webos_servicesdir}/com.palm.mediapermissions
    cp -rv ${S}/service/* ${D}${webos_servicesdir}/com.palm.mediapermissions
    rm -rf ${D}${webos_servicesdir}/com.palm.mediapermissions/sysbus

    # db8 kinds and permissions
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -vrf ${S}/service/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds/
    cp -vrf ${S}/service/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions/
}

FILES_${PN} += "${webos_servicesdir}"
