SUMMARY = "Service configurations run Open webOS daemons with systemd"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0+git"
SRCREV = "c5ca2fcfe3603270bfc17db3d72c6e2a8ed16930"

inherit webos_ports_ose_repo

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = ""

do_install() {
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${sbindir}

    for f in *.service ; do
        install -m 0644 ${S}/$f ${D}${systemd_unitdir}/system
    done

    install -m 0755 ${S}/populate-volatile.sh ${D}${sbindir}
}

FILES:${PN} += "${systemd_unitdir}/system"
FILES:${PN} += "${sbindir}"
