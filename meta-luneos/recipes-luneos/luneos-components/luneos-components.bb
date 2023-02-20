DESCRIPTION = "LuneOS QML components"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.5+git${SRCPV}"
SRCREV = "f544b9e7623da03dbf91960beadfb3e61d5c1aa2"

DEPENDS = "qtbase qtdeclarative luna-service2 luna-sysmgr-common libwebos-application qtdeclarative-native kf5bluezqt-mer"
RDEPENDS:${PN} = "qt5compat-qmlplugins"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit qt6-qmake
inherit webos_ports_repo
inherit webos_filesystem_paths
inherit pkgconfig

PACKAGES += "${PN}-examples"
FILES:${PN} += " \
    ${OE_QMAKE_PATH_QML}/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/LunaNext/ \
    ${OE_QMAKE_PATH_QML}/QtQuick/Controls/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/LunaWebEngineViewStyle/ \
"
FILES:${PN}-examples += " \
    ${webos_applicationsdir}/org.luneos.components.gallery \
"
