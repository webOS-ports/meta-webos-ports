SUMMARY = "The qml application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative qtwebengine luna-sysmgr-common libwebos-application"
RDEPENDS_${PN} += "qtwebengine-qmlplugins qtdeclarative-qmlplugins"

PV = "0.1.0-6+git${SRCPV}"
SRCREV = "98faf54a72d2426c4c9da99d3ab54d4102dec968"

inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt5

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
WEBOS_GIT_PARAM_BRANCH = "tofe/work"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_sysbus_containersdir}"
