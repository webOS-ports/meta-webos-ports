DESCRIPTION = "Platform specific configuration files for Luna SurfaceManager compositor"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    file://surface-manager.env \
"
SRC_URI:append:qemuall = "file://99-virtualbox-mouse.rules"

do_install() {
    install -d ${D}${sysconfdir}/surface-manager.d
    install -m 0644 ${WORKDIR}/surface-manager.env ${D}${sysconfdir}/surface-manager.d/
}

do_install:append:qemuall() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -v -m 0644 ${WORKDIR}/99-virtualbox-mouse.rules ${D}${sysconfdir}/udev/rules.d/99-virtualbox-mouse.rules
}

FILES:${PN} += " \
    ${sysconfdir}/surface-manager.d \
"

FILES:${PN}:append:qemuall = "${sysconfdir}/udev/rules.d"
