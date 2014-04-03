SUMMARY = "A terminal emulator with a custom virtual keyboard"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV = "1.0.3+gitr${SRCPV}"

DEPENDS = "qtbase"

SRC_URI = " \
    git://github.com/webOS-ports/fingerterm.git;protocol=git;branch=webOS-ports/master \
    file://appinfo.json"
SRCREV = "ec5794a31b837b4f8202a1768e5ada3966db9570"
S = "${WORKDIR}/git"

inherit qmake5

# Set path of qt5 headers as qmake5_base.bbclass sets this to just ${includedir} but
# actually it is ${includedir}/qt5
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

do_install() {
    install -d ${D}${webos_applicationsdir}/fingerterm

    install -m 0644 ${WORKDIR}/appinfo.json ${D}${webos_applicationsdir}/fingerterm/
    install -m 0755 ${WORKDIR}/build/fingerterm ${D}${webos_applicationsdir}/fingerterm/
    install -m 0644 ${S}/fingerterm.png ${D}${webos_applicationsdir}/fingerterm/icon.png

    # Always provide same version as we have in our recipe
    sed -i -e s:__VERSION__:${PV}:g ${D}${webos_applicationsdir}/fingerterm/appinfo.json
}

FILES_${PN}-dbg += "${webos_applicationsdir}/fingerterm/.debug"
FILES_${PN} += "${webos_applicationsdir}"
