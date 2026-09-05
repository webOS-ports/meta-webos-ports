DESCRIPTION = "LuneOS QML components"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.5+git"
SRCREV = "262d5b2281f1f67848512ada59648649f156a675"

# qtmultimedia and gstreamer are for LuneOS.Camera: it hands QML a droidcamsrc
# source to assign to CaptureSession.nativeVideoSource, which is the only way a
# QML app sees the camera on a Halium device. The SPI header it needs comes
# from qtmultimedia built with -DFEATURE_gstreamer_qt_api=ON; without it the
# factory compiles to a stub and reports itself unavailable.
DEPENDS = "qtbase qtdeclarative luna-service2 luna-sysmgr-common libwebos-application qtdeclarative-native bluez-qt qtmultimedia gstreamer1.0"
RDEPENDS:${PN} = "qt5compat-qmlplugins"

# LuneOS.Camera is useless without the plugin that provides droidcamsrc, but
# it has to be asked for only where it can be built: gst-droid is
# COMPATIBLE_MACHINE = "^halium$", and RRECOMMENDS is not the soft edge it
# looks like here. NO_RECOMMENDATIONS/BAD_RECOMMENDATIONS only drop a
# recommendation at rootfs-install time; bitbake's task graph folds
# RRECOMMENDS into the same rdepids set as RDEPENDS (taskdata.py), so an
# unbuildable recommendation fails this recipe and everything that pulls it
# in - on tissot that took out luna-next-cardshell, packagegroup-luneos-
# extended and luneos-dev-image with "Nothing RPROVIDES 'gst-droid'".
# Off Halium the camera goes through V4L2, which needs nothing from here.
RRECOMMENDS:${PN}:append:halium = " gst-droid"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

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
