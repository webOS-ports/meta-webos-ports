SUMMARY = "Service configurations run Open webOS daemons with systemd"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0+gitr${SRCPV}"
SRCREV = "a6e0618b0302efa182e906d73d5dcd8fe8f98005"

inherit webos_ports_repo

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=webosose"
S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = ""

do_install() {
    install -d ${D}${systemd_unitdir}/system

    for f in *.service ; do
        install -m 0644 ${S}/$f ${D}${systemd_unitdir}/system
    done
}

FILES_${PN} += "${systemd_unitdir}/system"
