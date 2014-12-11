DESCRIPTION = "Snowshoe is an open source project to build a cross platform browser \
application with multiple user interfaces while sharing the same core engine, based on Qt5 \
and WebKit2 technologies."
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4fbd65380cdd255951079008b364516c"

PV = "0.2.6+gitr${SRCPV}"

DEPENDS = "qtbase qtdeclarative qtwebkit"
RDEPENDS_${PN} += "qtwebkit-qmlplugins qtdeclarative-qmlplugins"

SRC_URI = " \
    git://gitorious.org/qt-apps/snowshoe.git;branch=master;protocol=git \
    file://appinfo.json"
SRCREV = "35afbe295043bd3e27357b8e987ec19b54bfd8c8"
S = "${WORKDIR}/git"

inherit webos_filesystem_paths
inherit qmake5

do_install() {
    install -d ${D}${webos_applicationsdir}/snowshoe

    install -m 0644 ${WORKDIR}/appinfo.json ${D}${webos_applicationsdir}/snowshoe/
    install -m 0755 ${WORKDIR}/build/snowshoe ${D}${webos_applicationsdir}/snowshoe/
    install -m 0644 ${S}/src/icons/snowshoe64.png ${D}${webos_applicationsdir}/snowshoe/icon.png

    # Always provide same version as we have in our recipe
    sed -i -e s:__VERSION__:${PV}:g ${D}${webos_applicationsdir}/snowshoe/appinfo.json
}

FILES_${PN}-dbg += "${webos_applicationsdir}/snowshoe/.debug"
FILES_${PN} += "${webos_applicationsdir}"
