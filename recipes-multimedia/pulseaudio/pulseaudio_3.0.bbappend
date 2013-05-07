PRINC := "${@int(PRINC) + 9}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://0200-fixing_snd_mixer_poll_descriptors_count_when_zero.patch \
    file://pulseaudio.upstart \
"

SYSTEM_CONF = "${D}${sysconfdir}/pulse/system.pa"
SYSTEM_CONF_TMP = "${SYSTEM_CONF}.temp"

do_install_append() {
    # Modify configuration to load android alsa plugin instead of default one
    grep -v "^load-module module-alsa" ${SYSTEM_CONF} > ${SYSTEM_CONF_TMP}
    echo "load-module module-alsa-sink device=android alternate_rate=16000 control=\"Master\"" >> ${SYSTEM_CONF_TMP}
    echo "load-module module-alsa-source device=android alternate_rate=16000 channels=1" >> ${SYSTEM_CONF_TMP}
    mv ${SYSTEM_CONF_TMP} ${SYSTEM_CONF}

    install -d ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/pulseaudio.upstart ${D}${webos_upstartconfdir}/pulseaudio
}

PACKAGES =+ "${PN}-upstart"
FILES_${PN}-upstart = "${webos_upstartconfdir}"
