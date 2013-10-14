DESCRIPTION = "Generate needed tokens for Open webOS on device boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

COMPATIBLE_MACHINE = "tuna|grouper"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    file://token-generator.sh \
    file://token-generator.service \
"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "token-generator.service"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/token-generator.sh ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/token-generator.service ${D}${systemd_unitdir}/system
}
