SUMMARY = "Browser application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0 & GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://COPYING;md5=2839aa2093f43edc5a4d4be71c6e526d \
"

DEPENDS = "qtbase qtdeclarative qtwebkit"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
    qtwebkit-qmlplugins \
"

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

WEBOS_VERSION = "0.5.0-14_db4d164ae325efd3a7ec4166c91ce7f96640e616"

inherit pkgconfig
inherit webos_enhanced_submissions
inherit webos_application

inherit cmake_qt5
inherit webos_cmake

INSANE_SKIP_${PN} = "libdir"
INSANE_SKIP_${PN}-dbg = "libdir"

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.browser"
FILES_${PN}-dbg += "${webos_applicationsdir}/${PN}/browserutils/.debug"
