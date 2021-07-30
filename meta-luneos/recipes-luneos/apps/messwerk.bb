SUMMARY = "An application for reading sensors, ported from SFOS"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=783b7e40cdfb4a1344d15b1f7081af66"

PV = "0.1.0+git${SRCPV}"
SRCREV = "3ab28261c15206991a5d97532a82ed8212f85277"

DEPENDS = "qtbase qtdeclarative qtsensors qtlocation"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit qmake5

APP_PATH = "${webos_applicationsdir}/${PN}"
EXTRA_QMAKEVARS_PRE = "\
    DEPLOYMENT_PATH=${APP_PATH} \
"

FILES:${PN} += "${APP_PATH}"
