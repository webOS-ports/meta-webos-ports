PRINC := "${@int(PRINC) + 6}"

DEFAULT_CONF = "${D}${sysconfdir}/pulse/default.pa"
DEFAULT_CONF_TMP = "${DEFAULT_CONF}.temp"

do_install_append() {
    # Modify configuration to load android alsa plugin instead of default one
    grep -v "^load-module module-alsa" ${DEFAULT_CONF} > ${DEFAULT_CONF_TMP}
    echo "load-module module-alsa-sink device=android alternate_rate=16000 control=\"Master\"" >> ${DEFAULT_CONF_TMP}
    echo "load-module module-alsa-source device=android alternate_rate=16000 channels=1" >> ${DEFAULT_CONF_TMP}
    mv ${DEFAULT_CONF_TMP} ${DEFAULT_CONF}
}
