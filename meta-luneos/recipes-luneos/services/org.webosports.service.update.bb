SUMMARY = "webOS Ports system update service"
SECTION = "webos/services"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"
SRCREV = "2cabcb863c4becef756660e5f246f07729686fea"

WEBOS_REPO_NAME = "org.webosports.update"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git/service"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files"

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
}

FILES_${PN} += "${webos_servicesdir}/${PN}"
