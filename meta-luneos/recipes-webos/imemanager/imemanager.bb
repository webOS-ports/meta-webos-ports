# Copyright (c) 2017-2021 LG Electronics, Inc.

SUMMARY = "Maliit Input Method Plugins"
DESCRIPTION = "Mallit-based virtual keyboard and input method engine for open webOS"
AUTHOR = "Pugalendhi Ganesan <pugalendhi.ganesan@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://oss-pkg-info.yaml;md5=7c010a972eb989740843ee18e1577536"

DEPENDS = "qtbase luna-service2 luna-prefs qt-features-webos qtdeclarative maliit-framework-webos qtdeclarative-native"
#We don't need the exotic languages for now, so let's simply remove them for now.
#RDEPENDS:${PN} += "libhangul sunpinyin pyzy qml-webos-bridge openwnn-webos"
RDEPENDS:${PN} += "qml-webos-bridge"

PV = "1.0.0-22+git${SRCPV}"
SRCREV = "9127a59721eb04ddf266e5bf18cdf2830c61b275"

inherit webos_qmake6
inherit webos_system_bus
inherit webos_ports_ose_repo
#inherit webos_qt_localization

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

WEBOS_REPO_NAME = "ime-manager"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

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
