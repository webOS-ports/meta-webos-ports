SUMMARY = "PDF viewer application based on Mozilla's pdf.js"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_enyojs_application
inherit webos_system_bus

PV = "1.0.0+gitr${SRCPV}"

SRCREV = "88b0b2ac0a7469499309024e72b241b8dd97b146"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service/dbus"

do_install_append() {
    install -d ${D}${webos_servicesdir}/org.webosports.app.pdf.service
    cp -r ${S}/service/* ${D}${webos_servicesdir}/org.webosports.app.pdf.service/
}

FILES_${PN} += "${webos_servicesdir}/org.webosports.app.pdf.service"
