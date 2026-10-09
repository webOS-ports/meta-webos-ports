SUMMARY = "LuneOS terminal emulator"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV = "0.1+git"
SRCREV = "f06473e4bcf48be07696644425ca5e06c825bcb7"
WEBOS_GIT_PARAM_BRANCH = "herrie/qt6-rework"

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
