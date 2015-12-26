DESCRIPTION = "Next generation webOS UI which meant to be a complete replacement for \
LunaSysMgr/WebAppMgr and is completely based on well known open source technologies like \
Qt 5 and Wayland."
LICENSE = "GPL-3.0 & LGPL-2.1 & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
    file://COPYING.LGPL;md5=fbc093901857fcd118f065f900982c24 \
"

DEPENDS = "qtbase qtdeclarative qtwayland luna-sysmgr-common extra-cmake-modules"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

PV = "0.2.0-26+git${SRCPV}"
SRCREV = "bacf378e07f03279fe40f8cfb1340bfe66ef737f"

# Otherwise there is conflict between None defined in Xlib.h and
# qtdeclarative's /usr/include/qt5/QtQuick/qsgtexture.h:59
# see http://lists.openembedded.org/pipermail/openembedded-core/2015-June/106351.html
# for details
CXXFLAGS += "-DMESA_EGL_NO_X11_HEADERS=1"

inherit pkgconfig
inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt5
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "luna-next.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/luna-next.service ${D}${systemd_unitdir}/system/
}

RDEPENDS_${PN} = "luna-next-conf xkeyboard-config"

FILES_${PN} += "${OE_QMAKE_PATH_QML}/LunaNext"
FILES_${PN}-dbg += " \
    ${OE_QMAKE_PATH_QML}/LunaNext/*/.debug \
    ${OE_QMAKE_PATH_QML}/LunaNext/*/*/.debug"
