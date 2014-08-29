SUMMARY = "webOS Ports Developer mode management service"
SECTION = "webos/services"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "0afb89e0a801e499e17ddf9092c4082a1c039db4"
SRC_URI = "git://github.com/webOS-ports/org.webosports.service.devmode;protocol=git;branch=master"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

do_install() {
    # Install service and remove unecessary things
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}
    rm -rf ${D}${webos_servicesdir}/${PN}/files
}

FILES_${PN} += "${webos_servicesdir}/${PN}"
