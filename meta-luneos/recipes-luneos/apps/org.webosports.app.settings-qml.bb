SUMMARY = "Settings app written from scratch in QML for LuneOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native"

inherit webos_ports_repo
inherit webos_filesystem_paths

inherit qt6-cmake
inherit webos_cmake
inherit webos_app
inherit pkgconfig

PV = "0.4.0-1+git"
SRCREV = "81ca75d423ba2f0a25533b3285ae9d8f806b7411"

WEBOS_GIT_PARAM_BRANCH = "qml-based"
WEBOS_REPO_NAME = "org.webosports.app.settings"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# We don't provide android-property-service on non-Android devices, so remove it in these cases to avoid LS2 warnings/errors

REMOVE_ANDROID_PROPERTY_SERVICE_CMD:halium = ""
REMOVE_ANDROID_PROPERTY_SERVICE_CMD = "sed -i 's/\"android-property-service.operation\", //g' ${D}/${webos_applicationsdir}/org.webosports.app.settings.deviceinfo/appinfo.json"

# The fingerprint panel only makes sense with the halium fingerprint stack
# (biomd + webos-fingerprint-adapter); drop the whole sub-app elsewhere
# so its requiredPermissions don't trip the LS2 ACG validation.
REMOVE_FINGERPRINT_APP_CMD:halium = ""
REMOVE_FINGERPRINT_APP_CMD = "rm -rf ${D}/${webos_applicationsdir}/org.webosports.app.settings.fingerprint"

REMOVE_FACEUNLOCK_APP_CMD:aarch64 = ""
REMOVE_FACEUNLOCK_APP_CMD = "rm -rf ${D}/${webos_applicationsdir}/org.webosports.app.settings.faceunlock"

do_install:append() {
    ${REMOVE_ANDROID_PROPERTY_SERVICE_CMD}
    ${REMOVE_FINGERPRINT_APP_CMD}
    ${REMOVE_FACEUNLOCK_APP_CMD}

    install -d ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    install -m 0644 ${S}/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds/
    install -m 0644 ${S}/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions/
}

FILES:${PN} += "${webos_sysconfdir}/db \
                ${webos_applicationsdir}/org.webosports.app.settings-common \
                ${webos_applicationsdir}/org.webosports.app.settings.accessibility \
                ${webos_applicationsdir}/org.webosports.app.settings.appearance \
                ${webos_applicationsdir}/org.webosports.app.settings.applications \
                ${webos_applicationsdir}/org.webosports.app.settings.backup \
                ${webos_applicationsdir}/org.webosports.app.settings.battery \
                ${webos_applicationsdir}/org.webosports.app.settings.bluetooth \
                ${webos_applicationsdir}/org.webosports.app.settings.cellbroadcast \
                ${webos_applicationsdir}/org.webosports.app.settings.certificate \
                ${webos_applicationsdir}/org.webosports.app.settings.dateandtime \
                ${webos_applicationsdir}/org.webosports.app.settings.deviceinfo \
                ${webos_applicationsdir}/org.webosports.app.settings.devmodeswitcher \
                ${webos_applicationsdir}/org.webosports.app.settings.display \
                ${webos_applicationsdir}/org.webosports.app.settings.encryption \
                ${webos_applicationsdir}/org.webosports.app.settings.esim \
                ${webos_applicationsdir}/org.webosports.app.settings.exhibitionpreferences \
                ${webos_applicationsdir}/org.webosports.app.settings.faceunlock \
                ${webos_applicationsdir}/org.webosports.app.settings.fingerprint \
                ${webos_applicationsdir}/org.webosports.app.settings.help \
                ${webos_applicationsdir}/org.webosports.app.settings.languagepicker \
                ${webos_applicationsdir}/org.webosports.app.settings.location \
                ${webos_applicationsdir}/org.webosports.app.settings.networksettings \
                ${webos_applicationsdir}/org.webosports.app.settings.nfc \
                ${webos_applicationsdir}/org.webosports.app.settings.notifications \
                ${webos_applicationsdir}/org.webosports.app.settings.printmanager \
                ${webos_applicationsdir}/org.webosports.app.settings.screenlock \
                ${webos_applicationsdir}/org.webosports.app.settings.searchpreferences \
                ${webos_applicationsdir}/org.webosports.app.settings.soundsandalerts \
                ${webos_applicationsdir}/org.webosports.app.settings.storage \
                ${webos_applicationsdir}/org.webosports.app.settings.tethering \
                ${webos_applicationsdir}/org.webosports.app.settings.textassist \
                ${webos_applicationsdir}/org.webosports.app.settings.updates \
                ${webos_applicationsdir}/org.webosports.app.settings.usb \
                ${webos_applicationsdir}/org.webosports.app.settings.vpn \
                ${webos_applicationsdir}/org.webosports.app.settings.wifi \
        ${datadir}/ls2 \
        "

RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
"
