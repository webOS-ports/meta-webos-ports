# Copyright (c) 2017-2024 LG Electronics, Inc.

SUMMARY = "Maliit Input Method Plugins"
DESCRIPTION = "Mallit-based virtual keyboard and input method engine for open webOS"
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=7c010a972eb989740843ee18e1577536 \
"

DEPENDS = "qtbase luna-service2 luna-prefs qt-features-webos qtdeclarative maliit-framework-webos qtdeclarative-native"
#We don't need the exotic languages for now, so let's simply remove them for now.
#RDEPENDS:${PN} += "libhangul sunpinyin pyzy qml-webos-bridge openwnn-webos"
RDEPENDS:${PN} += "qml-webos-bridge"

WEBOS_VERSION = "1.0.0-31_55da1c739acaedc2b26ad66f4fb14761905bba8a"
PR = "r7"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_qmake6
inherit webos_system_bus
inherit webos_public_repo
#inherit webos_qt_localization
inherit features_check
ANY_OF_DISTRO_FEATURES = "vulkan opengl"

#FIXME LuneOS Disable LS2 files from imemanager
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

WEBOS_REPO_NAME = "ime-manager"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-ime-manager-Fix-implicit-declaration-of-QML-Connecti.patch \
    file://0002-main.qml-Fix-syntax-error.patch \
    file://0003-com.webos.service.ime-Add-org.maliit-as-allowed-name.patch \
    file://0004-main.qml-Fix-the-signature-of-onResponse.patch \
"

WEBOS_LOCALIZATION_XLIFF_BASENAME = "imemanager"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

EXTRA_QMAKEVARS_PRE += "CONFIG+=disable-tests"
#Disable the exotic languages for now
#EXTRA_QMAKEVARS_PRE += "CONFIG+=enable-maliit-plugin-chinese"
#EXTRA_QMAKEVARS_PRE += "CONFIG+=enable-maliit-plugin-japanese"

EXTRA_QMAKEVARS_PRE += "LIBDIR=${STAGING_LIBDIR}"
EXTRA_QMAKEVARS_PRE += "WEBOS_INSTALL_BINS=${sbindir}"
EXTRA_QMAKEVARS_PRE += "MALIIT_PLUGIN_VERSION=${PV}"

FILES:${PN} += "${libdir}/maliit ${datadir}/maliit"

# FIXME-buildpaths!!!
# [WRP-10883] buildpath QA issues
# ERROR: QA Issue: File /usr/src/debug/imemanager/1.0.0-30/maliit-plugin-chinese/plugin/qrc_openjson.cpp in package imemanager-src contains reference to TMPDIR
# File /usr/src/debug/imemanager/1.0.0-30/maliit-plugin-global/plugin/qrc_json.cpp in package imemanager-src contains reference to TMPDIR
# File /usr/src/debug/imemanager/1.0.0-30/maliit-plugin-global/plugin/qrc_common-images-hd.cpp in package imemanager-src contains reference to TMPDIR
# File /usr/src/debug/imemanager/1.0.0-30/maliit-plugin-global/plugin/qrc_common-images.cpp in package imemanager-src contains reference to TMPDIR
# File /usr/src/debug/imemanager/1.0.0-30/maliit-plugin-global/plugin/qrc_common.cpp in package imemanager-src contains reference to TMPDIR
# File /usr/src/debug/imemanager/1.0.0-30/maliit-plugin-japanese/plugin/qrc_japanese-images.cpp in package imemanager-src contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
