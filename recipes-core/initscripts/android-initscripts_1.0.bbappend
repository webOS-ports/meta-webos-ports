FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += " \
    file://android_servicemanager.upstart \
    file://android_mediaserver.upstart \
    file://android_drmserver.upstart \
    file://android_sensorservice.upstart"

do_install_append() {
    mkdir -p ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/android_servicemanager.upstart ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/android_mediaserver.upstart ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/android_drmserver.upstart ${D}${webos_upstartconfdir}
    install -m 0644 ${WORKDIR}/android_sensorservice.upstart ${D}${webos_upstartconfdir}
}
