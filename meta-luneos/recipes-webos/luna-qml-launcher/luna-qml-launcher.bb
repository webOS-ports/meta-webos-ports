SUMMARY = "The qml application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative luna-sysmgr-common libwebos-application"
RDEPENDS:${PN} += "qtdeclarative-qmlplugins"

PV = "0.1.0-6+git${SRCPV}"
SRCREV = "46997b959847408d2885d8ca209c72e9bc6146fe"

inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt6
inherit pkgconfig

WEBOS_GIT_PARAM_BRANCH = "webosose-wam"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${WORKDIR}/git/files/sysbus"

SERVICE_NAME = "org.webosports.luna-qml-launcher"

do_install:append() {

    # ACG configuration files
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/${SERVICE_NAME}.perm.json
    sed "s|@WEBOS_INSTALL_SBINDIR@|$sbindir|" < ${S}/files/sysbus/${SERVICE_NAME}.role.json.in > ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
}

FILES:${PN} += "${webos_sysbus_containersdir}"
