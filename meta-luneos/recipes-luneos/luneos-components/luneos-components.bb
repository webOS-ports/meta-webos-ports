DESCRIPTION = "LuneOS QML components"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.4.2+git${SRCPV}"
SRCREV = "e3979027110efcf963d3ddc159703bbbdcf777ac"

DEPENDS = "qtbase qtdeclarative qtlocation qtquickcontrols qtquickcontrols2 luna-service2 kf5bluezqt-mer qtdeclarative-native"
RRECOMMENDS_${PN} += "qtlocation"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit qmake5
inherit webos_ports_repo
inherit webos_filesystem_paths

PACKAGES += "${PN}-examples"
FILES_${PN} += " \
    ${OE_QMAKE_PATH_QML}/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/QtQuick/Controls.2/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/QtQuick/Controls/Styles/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/LunaWebEngineViewStyle/ \
"
FILES_${PN}-examples += " \
    ${webos_applicationsdir}/org.luneos.components.gallery \
"

SRC_URI += "file://0001-Update-for-qtdeclarative-5.15.patch"
