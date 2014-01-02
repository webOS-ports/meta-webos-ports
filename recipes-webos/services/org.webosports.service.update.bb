SUMMARY = "webOS Ports system update service"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_system_bus

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "ca7caef88695c77c96cdd5252ffef18bf40f8043"
SRC_URI = "git://github.com/webOS-ports/org.webosports.update;protocol=git;branch=master"
S = "${WORKDIR}/git/service"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files"

do_install() {
    # Install service and remove unecessary things
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}
    rm -rf ${D}${webos_servicesdir}/${PN}/activities
    rm -rf ${D}${webos_servicesdir}/${PN}/files

    install -d ${D}${webos_sysconfdir}/activities
    cp -rv ${S}/activities/org.webosports.service.update ${D}${webos_sysconfdir}/activities/
}

FILES_${PN} += "${webos_servicesdir}/${PN}"
