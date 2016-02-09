SUMMARY = "Phone app written from scratch for LuneOS"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative libwebos-application"
RDEPENDS_${PN} += "qtdeclarative-qmlplugins libqofono"

inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt5
inherit webos_filesystem_paths
inherit webos_application

PV = "0.1.0-6+git${SRCPV}"
SRCREV = "f0cc37b9e05549b32b35fca67ddd67ae97b7880b"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN} += "\
    ${webos_applicationsdir}/${PN} \
    "
