SUMMARY = "License management service for the webOS ports project"
SECTION = "webos/services"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_repo
inherit webos_system_bus
inherit allarch

PV = "0.1.0+gitr${SRCPV}"
SRCREV = "fdd816e7c568a173e50d0e4b5d3bb2fcf1f84206"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
PALM_DIR = "${prefix}/palm"

do_install() {
    install -d ${D}${PALM_DIR}/services

    SERVICE_DIR="${PN}"
    install -d ${D}${PALM_DIR}/services/$SERVICE_DIR/
    cp -vf ${S}/*.js* ${D}${PALM_DIR}/services/$SERVICE_DIR/
}

FILES_${PN} += "${PALM_DIR}/services"
