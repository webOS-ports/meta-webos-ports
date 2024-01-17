# Copyright (c) 2017-2023 LG Electronics, Inc.

SUMMARY = "Maliit Input Method Plugins"
DESCRIPTION = "Mallit-based virtual keyboard and input method engine for webOS"
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

WEBOS_VERSION = "1.0.0-29_277aa343ce79866539ee1cafda091f06548b3f79"
PR = "r6"

PV = "1.0.0-29+git"
SRCREV = "277aa343ce79866539ee1cafda091f06548b3f79"

inherit webos_qmake6
inherit webos_system_bus
inherit webos_public_repo
#inherit webos_qt_localization

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

WEBOS_REPO_NAME = "ime-manager"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-ime-manager-Fix-implicit-declaration-of-QML-Connecti.patch \
    file://0002-main.qml-Fix-syntax-error.patch \
    file://0003-com.webos.service.ime-Add-org.maliit-as-allowed-name.patch \
    file://0004-main.qml-Fix-the-signature-of-onResponse.patch \
"
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

SERVICE_NAME = "com.webos.service.ime"
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/service"

do_install:append() {

    # ACG configuration files
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    #Replace the escaping characters (\) and the path placeholders
    sed "s|\$\$WEBOS_INSTALL_BINS|$sbindir|" < ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service.in > ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.service
    sed "s|\$\$WEBOS_INSTALL_BINS|$sbindir|g;s|[\]||g" < ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json.in > ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.perm.json ${D}${webos_sysbus_permissionsdir}/${SERVICE_NAME}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.api.json ${D}${webos_sysbus_apipermissionsdir}/${SERVICE_NAME}.api.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.groups.json ${D}${webos_sysbus_groupsdir}/${SERVICE_NAME}.groups.json
    #FixMe: Weird quirk installs role.json in services folder
    rm -rf ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.role.json
}

FILES:${PN} += "${libdir}/maliit ${datadir}/maliit"
