SUMMARY = "Keymanager implementation for webOS ports"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6c4db32a2fa8717faffa1d4f10136f47"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "440a65c76164b742366184bfa15c9f7b746a2beb"
SRC_URI = "git://github.com/webOS-ports/keymanager;protocol=git;branch=master"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service/sysbus"

do_install() {
    # the service itself
    install -d ${D}${webos_servicesdir}/com.palm.keymanager
    cp -rv ${S}/service/* ${D}${webos_servicesdir}/com.palm.keymanager
    rm -rf ${D}${webos_servicesdir}/com.palm.keymanager/sysbus
}

FILES_${PN} += "${webos_servicesdir}"
