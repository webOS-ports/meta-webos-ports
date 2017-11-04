DESCRIPTION = "LuneOS QML components"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.4.0+gitr${SRCPV}"
SRCREV = "ab0ab88a91d08b94c7e9078e16ca70f1f0f6f9fa"

DEPENDS = "qtbase qtdeclarative qtlocation qtwebengine qtquickcontrols qtquickcontrols2 luna-service2"
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
FILES_${PN}-dbg += " \
    ${OE_QMAKE_PATH_QML}/LuneOS/*/.debug \
    ${OE_QMAKE_PATH_QML}/QtQuick/Controls/Styles/LuneOS/.debug \
"
