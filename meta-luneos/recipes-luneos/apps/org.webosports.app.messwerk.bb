SUMMARY = "An application for reading sensors, ported from SFOS"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=783b7e40cdfb4a1344d15b1f7081af66"

PV = "0.1.0+git"
SRCREV = "4c3aa01d42138a7c06e781de3139713fc1573b3a"

DEPENDS = "qtbase qtdeclarative qtsensors qtpositioning"

WEBOS_REPO_NAME = "messwerk"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit qt6-qmake

APP_PATH = "${webos_applicationsdir}/${PN}"
EXTRA_QMAKEVARS_PRE = "\
    DEPLOYMENT_PATH=${APP_PATH} \
"

FILES:${PN} += "${APP_PATH}"
