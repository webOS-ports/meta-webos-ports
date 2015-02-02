DESCRIPTION = "LuneOS QML components"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.1.0+gitr${SRCPV}"
SRCREV = "c9c884190b5eb2bd929ceea4dad14d0771a60348"

DEPENDS = "qtbase qtdeclarative"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit qmake5
inherit webos_ports_repo

FILES_${PN} += "${OE_QMAKE_PATH_QML}/LuneOS/"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_QML}/LuneOS/*/.debug"
