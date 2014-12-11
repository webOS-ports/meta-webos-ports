SUMMARY = "File manager application for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_system_bus
inherit webos_filesystem_paths

PV = "1.0.0+gitr${SRCPV}"
SRCREV = "0b664e9e06c8381b84640c879b25b58a3a12aa84"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service/dbus"

do_install_append() {
    install -d ${D}${webos_servicesdir}/org.webosports.app.filemanager.service
    cp -r ${S}/service/* ${D}${webos_servicesdir}/org.webosports.app.filemanager.service/
}

FILES_${PN} += "${webos_servicesdir}/org.webosports.app.filemanager.service"
