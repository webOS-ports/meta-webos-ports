SUMMARY = "An application for reading sensors, ported from SFOS"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=783b7e40cdfb4a1344d15b1f7081af66"

PV = "0.1.0+git${SRCPV}"
SRCREV = "46572bc11957f06f71df150969a0e814080d1db1"

DEPENDS = "qtbase qtdeclarative qtsensors qtlocation"

SRC_URI = " \
    git://github.com/Tofee/Messwerk.git;branch=tofe/work \
"
S = "${WORKDIR}/git"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit qmake5

APP_PATH = "${webos_applicationsdir}/${PN}"
EXTRA_QMAKEVARS_PRE = "\
    DEPLOYMENT_PATH=${APP_PATH} \
"

FILES_${PN} += "${APP_PATH}"
