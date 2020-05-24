SUMMARY = "Settings app written from scratch in QML for LuneOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qtbase qtdeclarative"

inherit webos_ports_repo
inherit webos_filesystem_paths

inherit cmake_qt5
inherit webos_cmake
inherit webos_app

PV = "0.4.0-1+git${SRCPV}"
SRCREV = "be6af01496a1dd58ed78736491b3c46e5d23fd1d"

WEBOS_GIT_PARAM_BRANCH = "herrie/qml-based-acg"
WEBOS_REPO_NAME = "org.webosports.app.settings"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.settings-common \
                ${webos_applicationsdir}/org.webosports.app.settings.bluetooth \
                ${webos_applicationsdir}/org.webosports.app.settings.networksettings \
                ${webos_applicationsdir}/org.webosports.app.settings.vpn \
                ${webos_applicationsdir}/org.webosports.app.settings.wifi \
                ${webos_applicationsdir}/org.webosports.app.settings.backup \
                ${webos_applicationsdir}/org.webosports.app.settings.certificate \
                ${webos_applicationsdir}/org.webosports.app.settings.dateandtime \
                ${webos_applicationsdir}/org.webosports.app.settings.deviceinfo \
                ${webos_applicationsdir}/org.webosports.app.settings.devmodeswitcher \
                ${webos_applicationsdir}/org.webosports.app.settings.exhibitionpreferences \
                ${webos_applicationsdir}/org.webosports.app.settings.help \
                ${webos_applicationsdir}/org.webosports.app.settings.languagepicker \
                ${webos_applicationsdir}/org.webosports.app.settings.location \
                ${webos_applicationsdir}/org.webosports.app.settings.screenlock \
                ${webos_applicationsdir}/org.webosports.app.settings.searchpreferences \
                ${webos_applicationsdir}/org.webosports.app.settings.soundsandalerts \
                ${webos_applicationsdir}/org.webosports.app.settings.textassist \
                ${webos_applicationsdir}/org.webosports.app.settings.updates \
        ${datadir}/ls2 \
        "

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
"
