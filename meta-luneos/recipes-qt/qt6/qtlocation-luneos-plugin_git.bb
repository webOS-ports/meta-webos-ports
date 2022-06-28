SUMMARY = "QtPosition LuneOS plugin for qtlocation"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv3;md5=c1939be5579666be947371bc8120425f \
"

DEPENDS = "qtbase glib-2.0 luna-service2 qtlocation"

PV = "6.3.0+git${SRCPV}"
SRCREV = "62b1eb54f13f1d4349115bde95ba896239e5fd48"

inherit webos_ports_repo
inherit qt6-qmake
inherit pkgconfig

WEBOS_GIT_PARAM_BRANCH = "herrie/qt6"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};"
S = "${WORKDIR}/git"

FILES:${PN} += " \
    ${OE_QMAKE_PATH_PLUGINS} \
"
FILES:${PN}-dev += " \
    ${libdir}/cmake \
"
