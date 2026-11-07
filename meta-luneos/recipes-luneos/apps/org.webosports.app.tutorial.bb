SUMMARY = "Gesture Tutorial: back, Card View and closing apps, tried out"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "1.0.0+git"
SRCREV = "78d5774d65ed973ceef3b1885bf9578e2cca7915"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit cmake webos_cmake webos_application webos_filesystem_paths
# The LS2 role, manifest and client permissions come from appinfo.json's
# requiredPermissions: the reminder activity it creates calls SAM back as
# the app, which needs the app's groups under its bare id too.
inherit webos_app

FILES:${PN} += "${webos_applicationsdir}/org.webosports.app.tutorial"

# The QML modules it imports: QtQuick and Controls
# (qtdeclarative-qmlplugins), Eos.Window (qml-webos-framework), and
# LunaNext.Common, LuneOS.Components, LuneOS.Service, LuneOS.System and the
# LuneOS Controls style (luneos-components).
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    qml-webos-framework \
    luneos-components \
"
