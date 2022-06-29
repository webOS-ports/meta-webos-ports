SUMMARY = "An application for reading sensors, ported from SFOS"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=783b7e40cdfb4a1344d15b1f7081af66"

PV = "0.1.0+git${SRCPV}"
SRCREV = "ba690e4e150af237d0b20a242a60a780cbbf3f9c"

DEPENDS = "qtbase qtdeclarative qtsensors qtpositioning"

WEBOS_GIT_PARAM_BRANCH = "herrie/qt6"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit qt6-qmake

APP_PATH = "${webos_applicationsdir}/${PN}"
EXTRA_QMAKEVARS_PRE = "\
    DEPLOYMENT_PATH=${APP_PATH} \
"

FILES:${PN} += "${APP_PATH}"
