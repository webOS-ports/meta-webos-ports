
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://pulseaudio.upstart \
    file://pulseaudio.service \
"

SRC_URI += " \
    file://0001-core-make-dependencies-compile-for-64bit.patch \
    file://0002-combine-Fix-crash-in-output-freeing.patch \
    file://0003-build-Install-pulsecore-headers.patch \
    file://0004-daemon-Set-default-resampler-to-speex-fixed-2.patch \
    file://0005-bluetooth-Allow-leaving-transport-running-while-sink.patch \
    file://0006-bluetooth-device-Do-not-lose-transport-pointer-after.patch \
    file://0007-bluetooth-device-Default-to-using-A2DP-profile-initi.patch \
    file://0008-module-rescue-streams-Add-parameters-to-define-targe.patch \
    file://0009-bluetooth-util-Detect-transport-acquire-release-loop.patch \
    file://0010-suspend-on-idle-Ensure-we-still-time-out-if-a-stream.patch \
"

do_install_append() {
    install -d ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/pulseaudio.upstart ${D}${webos_upstartconfdir}/pulseaudio

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/pulseaudio.service ${D}${systemd_unitdir}/system
}

inherit systemd

SYSTEMD_PACKAGES = "${PN}-server"
SYSTEMD_SERVICE_${PN}-server = "pulseaudio.service"

PACKAGES =+ "${PN}-upstart"
FILES_${PN}-upstart = "${webos_upstartconfdir}"
