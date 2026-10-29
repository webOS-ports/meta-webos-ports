SUMMARY = "PDF viewer for LuneOS, drawing with poppler"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=db37b6ead2dec60580595736ff983452"

PV = "2.0.0+git"
PR = "r1"
SRCREV = "768915a0cba9e2d45c3a55473d50aaa997127685"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native poppler"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit pkgconfig
inherit webos_ports_repo
inherit webos_application
inherit webos_filesystem_paths

inherit qt6-cmake
inherit webos_cmake

# The QML plugin lives in the app's own directory, as Camera's does.
INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += " \
    ${webos_applicationsdir}/org.webosports.app.pdf \
    ${datadir}/luna-service2/roles.d/org.webosports.app.pdf.app.json \
    ${datadir}/luna-service2/manifests.d/org.webosports.app.pdf.manifest.json \
    ${datadir}/luna-service2/client-permissions.d/org.webosports.app.pdf.perm.json \
"

RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
"
