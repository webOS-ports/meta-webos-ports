SUMMARY = "Service configurations run Open webOS daemons with systemd"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0+gitr${SRCPV}"
SRCREV = "6197c8da2993f5df97ccbff192b15e22abb8d20f"

inherit webos_ports_repo

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " \
    luna-universalsearchmgr.service \
    luna-sysmgr.service \
    org.webosinternals.ipkgservice.service \
"

do_install() {
    install -d ${D}${systemd_unitdir}/system

    for f in *.service ; do
        install -m 0644 ${S}/$f ${D}${systemd_unitdir}/system
    done
}

# to catch .service files not listed in SYSTEMD_SERVICE (db8@.service and in some cases webos-telephonyd.service)
#FILES_${PN} += "${systemd_unitdir}/system"
