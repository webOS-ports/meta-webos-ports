DESCRIPTION = "Platform specific configuration files for the next generation webOS UI"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

PV = "1.0"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://environment.conf"

do_install() {
    install -d ${D}${sysconfdir}/luna-next
    install -m 0644 ${WORKDIR}/environment.conf ${D}${sysconfdir}/luna-next/
}
