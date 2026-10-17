SUMMARY = "System Monitor: processor, memory, storage, network and battery use, and the running processes"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=db37b6ead2dec60580595736ff983452"

PV = "1.0.0+git"
SRCREV = "6682c84c56635bcbc57fc86582ef2c7346d0c567"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit webos_application
inherit webos_filesystem_paths

inherit qt6-cmake
inherit webos_cmake

# The QML plugin (SysMonitorApp) sits in the app's own directory.
INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += " \
    ${webos_applicationsdir}/org.webosports.app.sysmonitor \
    ${datadir}/luna-service2/roles.d/org.webosports.app.sysmonitor.app.json \
    ${datadir}/luna-service2/manifests.d/org.webosports.app.sysmonitor.manifest.json \
    ${datadir}/luna-service2/client-permissions.d/org.webosports.app.sysmonitor.perm.json \
"

# The QML modules it imports: QtQuick, QtCore, Layouts and Controls
# (qtdeclarative-qmlplugins), and LuneOS.Forms, LuneOS.Service,
# LuneOS.System, LunaNext.Common and the LuneOS Controls style
# (luneos-components).
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    luneos-components \
"
