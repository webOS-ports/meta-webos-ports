SUMMARY = "Distribution specific configuration for Pulseaudio"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    file://webos-system.pa \
    file://pulseaudio.conf \
"

SRC_URI_append_pinephone = " \
    file://ucm/sun50i-a64-audio.conf \
    file://ucm/HiFi \
    file://ucm/VoiceCall \
"

do_install() {
    install -d ${D}${sysconfdir}/pulse
    install -m 0644 ${WORKDIR}/webos-system.pa ${D}${sysconfdir}/pulse/

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/pulseaudio.conf  ${D}${sysconfdir}/default/pulseaudio.conf
}

do_install_append_pinephone() {
    install -d ${D}${datadir}/alsa/ucm/sun50i-a64-audio
    install -m 0644 ${WORKDIR}/ucm/sun50i-a64-audio.conf ${D}${datadir}/alsa/ucm/sun50i-a64-audio/sun50i-a64-audio.conf
    install -m 0644 ${WORKDIR}/ucm/HiFi ${D}${datadir}/alsa/ucm/sun50i-a64-audio/HiFi
    install -m 0644 ${WORKDIR}/ucm/VoiceCall ${D}${datadir}/alsa/ucm/sun50i-a64-audio/VoiceCall
}

FILES_${PN} = "${sysconfdir}/pulse ${sysconfdir}/default ${datadir}/alsa/ucm"
