
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0020-stream-Return-error-in-case-a-client-peeks-to-early.patch \
    file://0022-inotify-wrapper-Quit-daemon-if-pid-file-is-removed.patch \
    file://0023-fixing_snd_mixer_poll_descriptors_count_when_zero.patch \
    file://0101-alsa-ucm-Make-combination-ports-have-lower-priority.patch \
    file://0102-combine-Fix-crash-in-output-freeing.patch \
    file://0103-resampler-Fix-peaks-resampler-s-channel-handling.patch \
    file://0201-ALSA-Add-extcon-Android-switch-jack-detection.patch \
    file://0202-dont-probe-ucm.patch \
    file://0203-card-Add-hook-before-profile-changes.patch \
    file://0204-Add-module-to-talk-to-the-Android-audio-hal-to-set-u.patch \
    file://pulseaudio.upstart \
    file://pulseaudio.service \
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

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/pulseaudio.service ${D}${systemd_unitdir}/system
}

inherit systemd

SYSTEMD_PACKAGES = "${PN}-server"
SYSTEMD_SERVICE_${PN}-server = "pulseaudio.service"

PACKAGES =+ "${PN}-upstart"
FILES_${PN}-upstart = "${webos_upstartconfdir}"
