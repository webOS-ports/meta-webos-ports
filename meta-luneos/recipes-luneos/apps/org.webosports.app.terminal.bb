SUMMARY = "LuneOS terminal emulator"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV = "0.1+git${SRCPV}"
SRCREV = "e47d824545db23b59c70c653942f741f6af9b198"

DEPENDS = "qtbase qtdeclarative qtquickcontrols2"
RDEPENDS:${PN} = "qmltermwidget "

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"


inherit webos_ports_repo
inherit webos_filesystem_paths
inherit qt6-cmake
inherit webos_cmake

APP_PATH = "${webos_applicationsdir}/${PN}"

EXTRA_QMAKEVARS_PRE = "\
    DEPLOYMENT_PATH=${APP_PATH} \
"

FILES:${PN} += "${APP_PATH}"
