SUMMARY = "Browser application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0 & GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://COPYING;md5=695ec3e59e33436b5777c14079d9fc69 \
"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
    qtwebkit-qmlplugins \
"

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

WEBOS_VERSION = "0.5.0-2_5f4a371e73b588a31aef5c69fccbe9be59aa504e"

inherit webos_component
inherit webos_daemon
inherit webos_system_bus
inherit webos_enhanced_submissions
inherit webos_application

do_install_append() {
    install -d ${D}${webos_applicationsdir}/org.webosports.app.browser/qml
    cp -av ${S}/qml/* ${D}${webos_applicationsdir}/org.webosports.app.browser/qml/
}

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.browser"
