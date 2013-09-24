FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://0001-Use-dbus-system-bus-instead-of-session-bus-for-dbus-.patch \
    file://0002-Set-XDG_RUNTIME_DIR-inside-maliit-server-statically.patch \
    file://maliit-server.conf \
    file://maliit-server.service \
"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "maliit-server.service"

do_install_append() {
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${WORKDIR}/maliit-server.conf ${D}${sysconfdir}/dbus-1/system.d/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/maliit-server.service ${D}${systemd_unitdir}/system/
}

pkg_postinst_${PN} () {
}

pkg_postrm_${PN} () {
}
