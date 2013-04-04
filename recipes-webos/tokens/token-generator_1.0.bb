DESCRIPTION = "Generate needed tokens for Open webOS on device boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

COMPATIBLE_MACHINE = "tuna|grouper"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://token-generator.sh"

PACKAGES = "${PN}"
FILES_${PN} = " \
  /etc/init.d/token-generator.sh \
  /etc/rcS.d/S65token-generator.sh"

do_install() {
    mkdir -p ${D}${sysconfdir}/init.d
    mkdir -p ${D}${sysconfdir}/rcS.d
    cp ${WORKDIR}/token-generator.sh ${D}${sysconfdir}/init.d/
    chmod +x ${D}${sysconfdir}/init.d/token-generator.sh
    ln -sf ../init.d/token-generator.sh ${D}${sysconfdir}/rcS.d/S65token-generator.sh
}
