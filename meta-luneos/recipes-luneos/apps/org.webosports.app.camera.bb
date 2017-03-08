SUMMARY = "Camera application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=84dcc94da3adb52b53ae4fa38fe49e5d \
"

PV = "0.0.1-1+git${SRCPV}"
SRCREV = "cafa683f29a0dd87a8480e19ee2a26bb7910e59c"

DEPENDS = "qtbase qtdeclarative qtwebengine"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
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

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.camera"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
    qtubuntu-camera \
"

