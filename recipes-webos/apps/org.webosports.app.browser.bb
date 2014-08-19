SUMMARY = "Browser application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "0.1.0+gitr${SRCPV}"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
    qtwebkit-qmlplugins \
"

SRCREV = "548d4166d8fbab2e8394d780f0d2174efd6702ac"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

# For the sake of the webOS build system we need to provide the webOS component version
# and even a submission number, even if we don't use any.
WEBOS_COMPONENT_VERSION = "0.1.0"
WEBOS_SUBMISSION = "0"
PV = "${WEBOS_COMPONENT_VERSION}+gitr${SRCPV}"

inherit webos_component

do_install_append() {
    install -d ${D}${webos_applicationsdir}/org.webosports.app.browser
    cp -av ${S}/* ${D}${webos_applicationsdir}/org.webosports.app.browser
}

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.browser"
