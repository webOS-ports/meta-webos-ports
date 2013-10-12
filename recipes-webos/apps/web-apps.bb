SUMMARY = "Several mobile optimized websites packaged as webOS application"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "0371c6557462feb8a38da3d953a0e95d75a6b2e2"
SRC_URI = "git://github.com/webOS-ports/web-apps;protocol=git;branch=master"
S = "${WORKDIR}/git"

inherit webos_public_repo
inherit webos_arch_indep

do_install() {
    install -d ${D}${webos_applicationsdir}
    cp -rv ${S}/org.webosports.app.* ${D}${webos_applicationsdir}
}

FILES_${PN} += "${webos_applicationsdir}"
