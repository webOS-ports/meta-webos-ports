FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

SRC_URI += " \
    file://android_finish.upstart \
    file://android_servicemanager.upstart \
    file://android_mediaserver.upstart \
    file://android_drmserver.upstart \
    file://android_sensorservice.upstart"

do_install_append() {
    mkdir -p ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/android_finish.upstart ${D}${webos_upstartconfdir}/android_finish
    install -m 0644 ${WORKDIR}/android_servicemanager.upstart ${D}${webos_upstartconfdir}/android_servicemanager
    install -m 0644 ${WORKDIR}/android_mediaserver.upstart ${D}${webos_upstartconfdir}/android_mediaserver
    install -m 0644 ${WORKDIR}/android_drmserver.upstart ${D}${webos_upstartconfdir}/android_drmserver
    install -m 0644 ${WORKDIR}/android_sensorservice.upstart ${D}${webos_upstartconfdir}/android_sensorservice
}
