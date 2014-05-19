SUMMARY = "Node.js service to ask for permission to media indexer database."
SECTION = "webos/services"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d32239bcb673463ab874e80d47fae504"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "eb741f598f4f858ba7596212adcb746eb3d67b6b"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service/sysbus"

do_install() {
    # the service itself
    install -d ${D}${webos_servicesdir}/com.palm.mediapermissions
    cp -rv ${S}/service/* ${D}${webos_servicesdir}/com.palm.mediapermissions
    rm -rf ${D}${webos_servicesdir}/com.palm.mediapermissions/sysbus
}

FILES_${PN} += "${webos_servicesdir}"
