SUMMARY = "Phone app written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative libwebos-application"
RDEPENDS_${PN} += "qtdeclarative-qmlplugins libqofono"

inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt5

PV = "0.1.0-6+git${SRCPV}"
SRCREV = "03e94248261214db57d3ed1152c56888a02e774f"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN}-dbg += "${webos_applicationsdir}/${PN}/.debug"
FILES_${PN} += "${webos_applicationsdir}/${PN}"
