SUMMARY = "QtPosition LuneOS plugin for qtlocation"
LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv3;md5=c1939be5579666be947371bc8120425f \
"

DEPENDS = "qtbase qtlocation glib-2.0 luna-service2"

PV = "5.12.3+git${SRCPV}"
SRCREV = "7849848e16528d860a03723514cdbe825e03a0a8"

inherit webos_ports_repo
inherit qmake5

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};"
S = "${WORKDIR}/git"

FILES_${PN} += " \
    ${OE_QMAKE_PATH_PLUGINS} \
"
FILES_${PN}-dev += " \
    ${libdir}/cmake \
"
