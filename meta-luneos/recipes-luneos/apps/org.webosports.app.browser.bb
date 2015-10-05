SUMMARY = "Browser application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "Apache-2.0 & GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://COPYING;md5=b008dcd25823510aea3435df7f15257a \
"

PV = "0.5.0-14+git${SRCPV}"
SRCREV = "b26a4b2777553045633ed40b0c14ea4c46274822"

DEPENDS = "qtbase qtdeclarative qtwebkit"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
WEBOS_GIT_PARAM_BRANCH = "master-webengine"
S = "${WORKDIR}/git"

inherit pkgconfig
inherit webos_ports_repo
inherit webos_application
inherit webos_filesystem_paths
inherit webos_tweaks

inherit cmake_qt5
inherit webos_cmake

INSANE_SKIP_${PN} = "libdir"
INSANE_SKIP_${PN}-dbg = "libdir"

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.browser"
FILES_${PN}-dbg += "${webos_applicationsdir}/${PN}/browserutils/.debug"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
    qtwebkit-qmlplugins \
"

