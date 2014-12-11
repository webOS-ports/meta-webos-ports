SUMMARY = "This is the web application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=0f93d2cf04b94ac3f04a789a1fb11ead \
    file://COPYING-Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57 \
"

DEPENDS = "qtbase qtdeclarative qtwebkit luna-sysmgr-common libwebos-application"
RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtwebkit-qmlplugins \
    libconnman-qt \
"

PV = "0.1.0-33+git${SRCPV}"
SRCREV = "b3660ff2439274e3d68d48ddbd2b000502b46afe"

inherit webos_ports_repo
inherit webos_system_bus
inherit webos_cmake_qt5

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0003-CMakeLists-check-only-for-libsystemd-pkg.patch \
"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_frameworksdir}"
