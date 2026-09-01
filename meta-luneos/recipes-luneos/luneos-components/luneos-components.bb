DESCRIPTION = "LuneOS QML components"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.5+git"
SRCREV = "7021c0f7c80c8e90c4f8bd76373025f2450fd5a2"

# qtmultimedia and gstreamer are for LuneOS.Camera: it hands QML a droidcamsrc
# source to assign to CaptureSession.nativeVideoSource, which is the only way a
# QML app sees the camera on a Halium device. The SPI header it needs comes
# from qtmultimedia built with -DFEATURE_gstreamer_qt_api=ON; without it the
# factory compiles to a stub and reports itself unavailable.
DEPENDS = "qtbase qtdeclarative luna-service2 luna-sysmgr-common libwebos-application qtdeclarative-native bluez-qt qtmultimedia gstreamer1.0"
RDEPENDS:${PN} = "qt5compat-qmlplugins"

# LuneOS.Camera is useless without the plugin that provides droidcamsrc,
# but the components package is not Halium-only, so recommend rather than
# require it.
RRECOMMENDS:${PN} += "gst-droid"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=herrie/more-fixes"

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
