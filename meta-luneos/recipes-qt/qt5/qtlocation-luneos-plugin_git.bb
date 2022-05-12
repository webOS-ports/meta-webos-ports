SUMMARY = "QtPosition LuneOS plugin for qtlocation"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv3;md5=c1939be5579666be947371bc8120425f \
"

DEPENDS = "qtbase qtlocation glib-2.0 luna-service2"

PV = "5.12.3+git${SRCPV}"
SRCREV = "2efa108bfeabee0bfcde19f438bddaf6af5e54a8"

inherit webos_ports_repo
inherit qt6-qmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};"
S = "${WORKDIR}/git"

FILES:${PN} += " \
    ${OE_QMAKE_PATH_PLUGINS} \
"
FILES:${PN}-dev += " \
    ${libdir}/cmake \
"
