SUMMARY = "Browser application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "0.1.0+gitr${SRCPV}"

DEPENDS = "qtwebkit"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
    qtwebkit-qmlplugins \
"

SRCREV = "7a147f5a8aed46c7a7edbae718ced1870bd94d85"
SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

# For the sake of the webOS build system we need to provide the webOS component version
# and even a submission number, even if we don't use any.
WEBOS_COMPONENT_VERSION = "0.1.0"
WEBOS_SUBMISSION = "0"
PV = "${WEBOS_COMPONENT_VERSION}+gitr${SRCPV}"

inherit webos_component

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.browser"
FILES_${PN}-dbg += "${webos_applicationsdir}/org.webosports.app.browser/.debug"
