DESCRIPTION = "Machine specific configuration file for the Nyx hardware abstraction layer"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://nyx.conf"

do_install() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/nyx.conf ${D}${sysconfdir}
}
