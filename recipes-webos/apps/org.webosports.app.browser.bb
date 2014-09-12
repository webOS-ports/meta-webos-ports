SUMMARY = "Browser application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0 & GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://COPYING;md5=1d79dd5a6b0f3d9bd194c5e3a97986ad \
"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
    qtwebkit-qmlplugins \
"

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

WEBOS_VERSION = "0.5.0-7_13b4b8bbdafeaec75fc756dd62e0769aba9b3f98"

inherit webos_component
inherit webos_daemon
inherit webos_enhanced_submissions
inherit webos_application

do_install_append() {
    install -d ${D}${webos_applicationsdir}/org.webosports.app.browser/qml
    cp -av ${S}/qml/* ${D}${webos_applicationsdir}/org.webosports.app.browser/qml/
}

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.browser"
