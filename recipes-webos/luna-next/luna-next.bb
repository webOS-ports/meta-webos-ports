DESCRIPTION = "Next generation webOS UI which meant to be a complete replacement for \
LunaSysMgr/WebAppMgr and is completely based on well known open source technologies like \
Qt 5 and Wayland."
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "qtbase qtdeclarative qtwayland luna-sysmgr-common"

SRC_URI = "git://github.com/webOS-ports/luna-next.git;branch=master;protocol=git"
# needed for newer qtwayland when used together with qtbase-5.2.1
# SRC_URI += "file://0001-Revert-plugins-compositor-check-for-qt-5.2.1.patch"

S = "${WORKDIR}/git"

WEBOS_VERSION = "0.1.0-17_0d76132154349f93ffaee7a967972370ad3c0982"

inherit webos_component
inherit webos_daemon
inherit webos_system_bus
inherit webos_enhanced_submissions

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

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

SRC_URI += "file://0001-Fix-fpermissive-issues.patch"
