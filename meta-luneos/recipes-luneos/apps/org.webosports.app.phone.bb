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

PE = "4"
PV = "0.1.1-0+git"

# master, which has pull requests #91 (post-dial pause), #92 (video calls),
# #106 (count a call's duration in seconds), #107 (supplementary services read
# only when asked) and #108 (VoLTE for calls over IMS).
SRCREV = "a982e0c7feb8bdb610108431fd9b216a60d72f4d"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

FILES:${PN} += "\
    ${webos_applicationsdir}/${PN} \
    "
