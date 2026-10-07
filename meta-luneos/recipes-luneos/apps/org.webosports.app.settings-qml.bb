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
SRCREV = "02fafb0aaa6267ba5eb444c88e3e26bff4f81831"

# This app is developed on qml-based, not master.
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
                ${webos_applicationsdir}/org.webosports.app.settings.certificate \
                ${webos_applicationsdir}/org.webosports.app.settings.dateandtime \
                ${webos_applicationsdir}/org.webosports.app.settings.deviceinfo \
                ${webos_applicationsdir}/org.webosports.app.settings.devmodeswitcher \
                ${webos_applicationsdir}/org.webosports.app.settings.display \
                ${webos_applicationsdir}/org.webosports.app.settings.encryption \
                ${webos_applicationsdir}/org.webosports.app.settings.exhibitionpreferences \
                ${webos_applicationsdir}/org.webosports.app.settings.help \
                ${webos_applicationsdir}/org.webosports.app.settings.languagepicker \
                ${webos_applicationsdir}/org.webosports.app.settings.location \
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

# The panels of hardware a machine may not have are packages of their own, so that a machine that lacks the
# hardware does not get a settings page for it (the Galaxy Tab Pro 10.1 had NFC and Fingerprint panels with
# neither). packagegroup-luneos-extended adds each one with the stack it belongs to, which is decided by
# MACHINE_FEATURES: nfc, fingerprint, esim, a front camera for face unlock, and phone (a modem) for
# cell broadcast and the cellular network settings.
PACKAGES =+ "${PN}-nfc ${PN}-fingerprint ${PN}-esim ${PN}-faceunlock ${PN}-cellbroadcast ${PN}-networksettings"
FILES:${PN}-nfc = "${webos_applicationsdir}/org.webosports.app.settings.nfc"
FILES:${PN}-fingerprint = "${webos_applicationsdir}/org.webosports.app.settings.fingerprint"
FILES:${PN}-esim = "${webos_applicationsdir}/org.webosports.app.settings.esim"
FILES:${PN}-faceunlock = "${webos_applicationsdir}/org.webosports.app.settings.faceunlock"
FILES:${PN}-cellbroadcast = "${webos_applicationsdir}/org.webosports.app.settings.cellbroadcast"
# "Network Settings" is the cellular page: mobile data, roaming, APNs and the SIMs.
FILES:${PN}-networksettings = "${webos_applicationsdir}/org.webosports.app.settings.networksettings"
# The fingerprint and face unlock apps are still removed on the architectures where their stacks do not
# exist (see the REMOVE_* commands above); the package is then empty, and an empty package that a package
# group asks for must still exist.
ALLOW_EMPTY:${PN}-nfc = "1"
ALLOW_EMPTY:${PN}-fingerprint = "1"
ALLOW_EMPTY:${PN}-esim = "1"
ALLOW_EMPTY:${PN}-faceunlock = "1"
ALLOW_EMPTY:${PN}-cellbroadcast = "1"
ALLOW_EMPTY:${PN}-networksettings = "1"
RDEPENDS:${PN}-nfc = "${PN}"
RDEPENDS:${PN}-fingerprint = "${PN}"
RDEPENDS:${PN}-esim = "${PN}"
RDEPENDS:${PN}-faceunlock = "${PN}"
RDEPENDS:${PN}-cellbroadcast = "${PN}"
RDEPENDS:${PN}-networksettings = "${PN}"
