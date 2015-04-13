FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://dbus-session.service \
    file://startup-dbus-session.sh \
"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/dbus-session.service ${D}${systemd_unitdir}/system/

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/startup-dbus-session.sh ${D}${bindir}/
}

FILES_${PN} += "${bindir} ${systemd_unitdir}"
