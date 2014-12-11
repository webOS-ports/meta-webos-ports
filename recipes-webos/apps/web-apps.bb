SUMMARY = "Several mobile optimized websites packaged as webOS application"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "49fe655f17aa61750f6febde5d37787cc3e63a3d"
SRC_URI = "git://github.com/webOS-ports/web-apps;protocol=git;branch=master"
S = "${WORKDIR}/git"

inherit webos_public_repo
inherit allarch

do_install() {
    install -d ${D}${webos_applicationsdir}
    cp -rv ${S}/org.webosports.app.* ${D}${webos_applicationsdir}
}

FILES_${PN} += "${webos_applicationsdir}"
