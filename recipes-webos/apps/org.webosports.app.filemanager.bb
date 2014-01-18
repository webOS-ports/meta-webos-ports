SUMMARY = "File manager application for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_enyojs_application

PV = "1.0.0+gitr${SRCPV}"

SRCREV = "ccad5a33920f0b53532bcca644c57c368d5a33ed"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${webos_servicesdir}/org.webosports.app.filemanager.service
    cp -r ${S}/service/* ${D}${webos_servicesdir}/org.webosports.app.filemanager.service/
}

FILES_${PN} += "${webos_servicesdir}/org.webosports.app.filemanager.service"
