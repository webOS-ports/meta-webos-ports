DESCRIPTION = "LuneOS QML components"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.5+git${SRCPV}"
SRCREV = "0140013eb1a8f1463739a28d276d69dcb9360c43"

#FIXME port kf5bluezqt-mer to Qt6 (Using yocto-meta-kf5 instead) bluez-qt 
DEPENDS = "qtbase qtdeclarative luna-service2 luna-sysmgr-common libwebos-application qtdeclarative-native"

WEBOS_GIT_PARAM_BRANCH = "herrie/qt6-new"
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
    ${OE_QMAKE_PATH_QML}/QtQuick/Controls.2/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/QtQuick/Controls/Styles/LuneOS/ \
    ${OE_QMAKE_PATH_QML}/LunaWebEngineViewStyle/ \
"
FILES:${PN}-examples += " \
    ${webos_applicationsdir}/org.luneos.components.gallery \
"
