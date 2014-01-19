SUMMARY = "License management service for the webOS ports project"
SECTION = "webos/services"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_public_repo
inherit webos_system_bus
inherit webos_arch_indep

PV = "0.1.0+gitr${SRCPV}"
SRCREV = "10dc17054730dc0e05229373fd2c9033730f069e"
SRC_URI = "git://github.com/webOS-ports/${PN};branch=master;protocol=git"
S = "${WORKDIR}/git"

PALM_DIR = "${prefix}/palm"

do_install() {
    install -d ${D}${PALM_DIR}/services

    SERVICE_DIR="${PN}"
    install -d ${D}${PALM_DIR}/services/$SERVICE_DIR/
    cp -vf ${S}/*.js* ${D}${PALM_DIR}/services/$SERVICE_DIR/
}

FILES_${PN} += "${PALM_DIR}/services"
