SUMMARY = "LuneOS terminal emulator"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV = "0.1+git"
SRCREV = "3b13b9a4f543a1e3bf169fd8e4eed253b19012b5"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native"
RDEPENDS:${PN} = "qmltermwidget "

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit qt6-cmake
inherit webos_cmake

APP_PATH = "${webos_applicationsdir}/${PN}"

EXTRA_QMAKEVARS_PRE = "\
    DEPLOYMENT_PATH=${APP_PATH} \
"

FILES:${PN} += "${APP_PATH}"
