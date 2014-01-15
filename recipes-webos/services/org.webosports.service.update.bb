SUMMARY = "webOS Ports system update service"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "32304d64561d814e1e2626d4d0424db8d6277aab"
SRC_URI = "git://github.com/webOS-ports/org.webosports.update;protocol=git;branch=master"
S = "${WORKDIR}/git/service"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "webos-system-update.service"

do_install() {
    # Install service and remove unecessary things
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}
    rm -rf ${D}${webos_servicesdir}/${PN}/activities
    rm -rf ${D}${webos_servicesdir}/${PN}/files

    install -d ${D}${webos_sysconfdir}/activities
    cp -rv ${S}/activities/org.webosports.service.update ${D}${webos_sysconfdir}/activities/

    chmod +x ${D}${webos_servicesdir}/${PN}/start-update.sh
    chmod +x ${D}${webos_servicesdir}/${PN}/run-update.sh
    chmod +x ${D}${webos_servicesdir}/${PN}/download-updates.sh

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/webos-system-update.service ${D}${systemd_unitdir}/system
}

FILES_${PN} += "${webos_servicesdir}/${PN}"
