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

PE = "1"
PV = "0.1.1-0+git"

# master, which has pull requests #91 (post-dial pause) and #92 (video calls).
SRCREV = "bac25ac02d4feee8ba9c05bf62a7ddcb98f9417f"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

FILES:${PN} += "\
    ${webos_applicationsdir}/${PN} \
    "
