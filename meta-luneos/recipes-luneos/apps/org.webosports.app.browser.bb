SUMMARY = "Browser application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0 & GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://COPYING;md5=4781c67bae2fdeb25779230fb39e1f69 \
"

PV = "0.5.0-14+git${SRCPV}"
SRCREV = "4b5398142ba67fcd63f73e07b84d9440faab7eb8"

DEPENDS = "qtbase qtdeclarative qtwebengine"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit pkgconfig
inherit webos_ports_repo
inherit webos_application
inherit webos_filesystem_paths
inherit webos_tweaks
inherit webos_app

inherit cmake_qt5
inherit webos_cmake

INSANE_SKIP_${PN} = "libdir"
INSANE_SKIP_${PN}-dbg = "libdir"

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.browser"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
    qtwebengine-qmlplugins \
"

