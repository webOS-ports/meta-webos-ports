SUMMARY = "The qml application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative qtwebengine luna-sysmgr-common libwebos-application"
RDEPENDS_${PN} += "qtwebengine-qmlplugins qtdeclarative-qmlplugins"

PV = "0.1.0-6+git${SRCPV}"
SRCREV = "cd2cfd6ff7431a3362b26daad752f7c4716771b7"

inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt5

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
WEBOS_GIT_PARAM_BRANCH = "herrie/ls2-fix"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_sysbus_containersdir}"
