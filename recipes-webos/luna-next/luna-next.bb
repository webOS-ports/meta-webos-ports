DESCRIPTION = "Next generation webOS UI which meant to be a complete replacement for \
LunaSysMgr/WebAppMgr and is completely based on well known open source technologies like \
Qt 5 and Wayland."
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=fc045d5b44f26b8b01a2fba30ceb524f"

PV = "0.1.0+gitr${SRCPV}"

DEPENDS = "qtbase qtdeclarative qtwayland qtwebkit luna-sysmgr-common"

RDEPENDS_${PN} = " \
    qtgraphicaleffects-qmlplugins \
"

SRC_URI = "git://github.com/webOS-ports/luna-next.git;branch=master;protocol=git"
SRCREV = "54d9c7935ac7d26ec79de98189fce6cd637c178d"
S = "${WORKDIR}/git"

inherit cmake_qt5
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "luna-next.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/luna-next.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${OE_QMAKE_PATH_IMPORTS}/LunaNext"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_IMPORTS}/LunaNext/.debug"
