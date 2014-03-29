
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-stream-Return-error-in-case-a-client-peeks-to-early.patch \
    file://0002-inotify-wrapper-Quit-daemon-if-pid-file-is-removed.patch \
    file://0003-Avoid-abort-when-poll-descriptor-is-0.patch \
    file://0004-ALSA-Add-extcon-Android-switch-jack-detection.patch \
    file://0005-alsa-ucm-trust-that-the-person-writing-the-UCM-file-.patch \
    file://0006-card-Add-hook-before-profile-changes.patch \
    file://0007-Add-module-to-talk-to-the-Android-audio-hal-to-set-u.patch \
    file://0008-alsa-extcon-don-t-use-is_input.patch \
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
