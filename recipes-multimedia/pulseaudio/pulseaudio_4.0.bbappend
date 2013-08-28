
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0200-fixing_snd_mixer_poll_descriptors_count_when_zero.patch \
    file://0201-ALSA-Add-extcon-Android-switch-jack-detection.patch \
    file://0202-dont-probe-ucm.patch \
    file://0203-card-Add-hook-before-profile-changes.patch \
    file://0204-Add-module-to-talk-to-the-Android-audio-hal-to-set-u.patch \
    file://pulseaudio.upstart \
    file://system.pa \
    file://daemon.conf \
"

do_install_append() {
    install -d ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/pulseaudio.upstart ${D}${webos_upstartconfdir}/pulseaudio

    # override configuration with our own
    rm ${D}${sysconfdir}/pulse/daemon.conf
    install -m 0644 ${WORKDIR}/daemon.conf ${D}${sysconfdir}/pulse
    rm ${D}${sysconfdir}/pulse/system.pa
    install -m 0644 ${WORKDIR}/system.pa ${D}${sysconfdir}/pulse

    # we don't need to run pulseaudio for a session so drop it's configuration
    rm ${D}${sysconfdir}/pulse/default.pa
}

PACKAGES =+ "${PN}-upstart"
FILES_${PN}-upstart = "${webos_upstartconfdir}"
