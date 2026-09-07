SUMMARY = "webOS Ports First Use application"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"

PV = "0.3.0-7+git"
SRCREV = "e0f0e16869deb3766856808e3be82b431ca4b346"

DEPENDS += "qtbase qtdeclarative qtdeclarative-native"

inherit webos_ports_repo
inherit webos_application
inherit webos_cmake_qt6
inherit webos_filesystem_paths
inherit webos_app

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += "${datadir}/luneos-license-agreements"
