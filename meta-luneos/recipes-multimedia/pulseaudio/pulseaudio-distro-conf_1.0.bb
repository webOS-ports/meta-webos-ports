SUMMARY = "Distribution specific configuration for Pulseaudio"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Nothing but local files, so nothing ever lands in the default
# S = "${UNPACKDIR}/${BP}" and do_unpack warns about it.
S = "${UNPACKDIR}"

# /etc/default/pulseaudio.conf used to be installed here purely to set
# PULSE_SCRIPT=/etc/pulse/webos-system.pa, but nothing ever read that variable --
# pulseaudio.service's ExecStart names the script directly now, so the file had no
# remaining content or purpose.
# webos-virtual-devices.pa is deliberately not per-machine: the names it declares
# have to match module-palm-policy's virtualsinkmap[]/virtualsourcemap[], which is
# the same everywhere, and a missing one aborts pulseaudio rather than degrading.
# Machine-specific webos-system.pa files include it.
SRC_URI = " \
    file://webos-system.pa \
    file://webos-virtual-devices.pa \
    file://droid-audio-config-gen \
"

do_install() {
    install -d ${D}${sysconfdir}/pulse
    install -m 0644 ${UNPACKDIR}/webos-system.pa ${D}${sysconfdir}/pulse/
    install -m 0644 ${UNPACKDIR}/webos-virtual-devices.pa ${D}${sysconfdir}/pulse/
}

# Halium: generate the 16-bit-input audio policy copy for module-droid-card
# before pulseaudio starts (see the script and the droid section of
# webos-system.pa).
do_install:append:halium() {
    install -d ${D}${libexecdir}
    install -m 0755 ${UNPACKDIR}/droid-audio-config-gen ${D}${libexecdir}/
    install -d ${D}${systemd_system_unitdir}/pulseaudio.service.d
    printf '[Service]\nExecStartPre=%s/droid-audio-config-gen\n' "${libexecdir}" \
        > ${D}${systemd_system_unitdir}/pulseaudio.service.d/droid-audio.conf
}

# webos-virtual-devices.pa creates the virtual sources (record, webcall,
# btcallsource, ...) with module-null-source, but oe-core only lists
# module-null-sink in RDEPENDS:pulseaudio-server, so nothing pulled the source
# half in and all twelve loads failed with "cannot open shared object file".
#
# That is not a cosmetic log error. module-palm-policy's
# route_source_output_new_hook_callback() resolves a capture stream that names
# no device with pa_namereg_get(c, "record", ...) followed by
# pa_assert(source != NULL) - so with the virtual source missing, the first
# such stream aborts PulseAudio outright (SIGABRT, core dumped), taking audio
# down with it. gst-launch pulsesrc and the camera app's recording pipeline
# both hit exactly this.
RDEPENDS:${PN} += "pulseaudio-module-null-source"

FILES:${PN} = "${sysconfdir}/pulse"
FILES:${PN}:append:halium = " ${libexecdir}/droid-audio-config-gen ${systemd_system_unitdir}/pulseaudio.service.d"
