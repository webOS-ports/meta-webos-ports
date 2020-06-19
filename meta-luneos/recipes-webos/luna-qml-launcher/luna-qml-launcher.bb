SUMMARY = "The qml application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative qtwebengine luna-sysmgr-common libwebos-application"
RDEPENDS_${PN} += "qtwebengine-qmlplugins qtdeclarative-qmlplugins"

PV = "0.1.0-6+git${SRCPV}"
SRCREV = "c59d517641359a10643484e86f393ba68f6dd246"

inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt5

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${WORKDIR}/git/files/sysbus"

SERVICE_NAME = "org.webosports.luna-qml-launcher"

do_install_append() {

    # ACG configuration files
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/${SERVICE_NAME}.perm.json
    sed "s|@WEBOS_INSTALL_SBINDIR@|$sbindir|" < ${S}/files/sysbus/${SERVICE_NAME}.role.json.in > ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
}

FILES_${PN} += "${webos_sysbus_containersdir}"
