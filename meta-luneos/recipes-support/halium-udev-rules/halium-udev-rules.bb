SUMMARY = "Apply Android ueventd device permissions under Halium"
DESCRIPTION = "Android assigns device-node ownership through ueventd*.rc, which \
Halium does not run. Without a translation to udev rules the nodes are created \
root:root 0600 and Android daemons running as their own users cannot open the \
hardware they need. Droidian and UBports ship per-device rules generated the \
same way; this generates them at boot from whatever the device mounts."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
    file://halium-generate-udev-rules \
    file://halium-udev-rules.service \
"

S = "${UNPACKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "halium-udev-rules.service"
SYSTEMD_AUTO_ENABLE = "enable"

RDEPENDS:${PN} = "udev"

COMPATIBLE_MACHINE = "^halium$"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/halium-generate-udev-rules ${D}${bindir}/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/halium-udev-rules.service ${D}${systemd_system_unitdir}/
}
