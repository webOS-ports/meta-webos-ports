SUMMARY = "QtPosition LuneOS plugin for qtlocation/qtpositioning"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv3;md5=c1939be5579666be947371bc8120425f \
"

DEPENDS = "qtbase glib-2.0 luna-service2 qtpositioning"

PV = "6.3.0+git"
SRCREV = "258b292c108214b580acee9e700d63627c2e71e3"

inherit webos_ports_repo
inherit qt6-qmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

FILES:${PN} += " \
    ${OE_QMAKE_PATH_PLUGINS} \
"
FILES:${PN}-dev += " \
    ${libdir}/cmake \
"
