require mmsd.inc

SRCREV = "06acadd1f28107c876a7662be9517723a3203128"
PV = "0.0+gitr${SRCPV}"

SRC_URI  = " \
    git://git.kernel.org/pub/scm/network/ofono/mmsd.git;protocol=git;branch=master \
    file://0001-Switch-to-dbus-system-bus.patch \
    file://mmsd.conf \
    file://mmsd.service \
"
S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"

inherit autotools-brokensep systemd

do_configure_prepend () {
  ${S}/bootstrap
}

do_install_append() {
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${WORKDIR}/${PN}.conf ${D}${sysconfdir}/dbus-1/system.d/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${PN}.service ${D}${systemd_unitdir}/system/
}
