SUMMARY = "Camera application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=84dcc94da3adb52b53ae4fa38fe49e5d \
"

PV = "0.0.2-1+git${SRCPV}"
SRCREV = "a407b0443e7eb934c717723fbdedbd54ae631520"

DEPENDS = "qtbase qtdeclarative qtwebengine qtmultimedia"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit pkgconfig
inherit webos_ports_repo
inherit webos_application
inherit webos_filesystem_paths
inherit webos_tweaks

inherit cmake_qt5
inherit webos_cmake

INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += "${webos_applicationsdir}/org.webosports.app.camera"

RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
"

