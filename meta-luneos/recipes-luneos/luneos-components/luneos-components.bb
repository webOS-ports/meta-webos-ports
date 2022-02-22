DESCRIPTION = "LuneOS QML components"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.4.2+git${SRCPV}"
SRCREV = "3ef9a5dcd2e5fad6b105996ae44d53830696aa49"

DEPENDS = "qtbase qtdeclarative qtlocation qtquickcontrols qtquickcontrols2 luna-service2 libwebos-application kf5bluezqt-mer qtdeclarative-native"

RRECOMMENDS:${PN} += "qtlocation"

WEBOS_GIT_PARAM_BRANCH = "webosose-wam"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit qmake5
inherit webos_ports_repo
inherit webos_filesystem_paths
inherit pkgconfig

PACKAGES += "${PN}-examples"
FILES:${PN} += " \
    ${OE_QMAKE_PATH_QML}/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/QtQuick/Controls.2/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/QtQuick/Controls/Styles/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/LunaWebEngineViewStyle/ \
"
FILES:${PN}-examples += " \
    ${webos_applicationsdir}/org.luneos.components.gallery \
"
