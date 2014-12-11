SUMMARY = "Distribution specific configuration for Pulseaudio"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://webos-system.pa \
    file://pulseaudio.conf \
"

do_install() {
    install -d ${D}${sysconfdir}/pulse
    install -m 0644 ${WORKDIR}/webos-system.pa ${D}${sysconfdir}/pulse/

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/pulseaudio.conf  ${D}${sysconfdir}/default/pulseaudio.conf
}

FILES_${PN} = "${sysconfdir}/pulse ${sysconfdir}/default"
