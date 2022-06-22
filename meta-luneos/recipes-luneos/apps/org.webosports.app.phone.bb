SUMMARY = "Phone app written from scratch for LuneOS"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative libqofono"
RDEPENDS:${PN} += "qtdeclarative-qmlplugins voicecall"

inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt6
inherit webos_filesystem_paths
inherit webos_application
inherit webos_tweaks
inherit webos_app

PV = "0.1.0-6+git${SRCPV}"
SRCREV = "423c139e4483a69a1b3ff81aa6178608cf56ed9f"

WEBOS_GIT_PARAM_BRANCH = "tofe/qt6"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES:${PN} += "\
    ${webos_applicationsdir}/${PN} \
    "
