require mmsd.inc

SRCREV = "c259df5ca9f436ecfe2f614c752080bbcfb95f0b"
PV = "0.0+git"

SRC_URI  = " \
    git://git.kernel.org/pub/scm/network/ofono/mmsd.git;protocol=git;branch=master \
    file://0001-Switch-to-dbus-system-bus.patch \
    file://mmsd.conf \
    file://mmsd.service \
    file://mmsd-migrate-store \
"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "${PN}.service"

inherit autotools-brokensep systemd useradd

# mmsd.service runs the daemon as this user; see the unit.
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --user-group --home-dir ${localstatedir}/lib/mmsd --no-create-home --shell /usr/sbin/nologin mmsd"

do_configure:prepend () {
  ${S}/bootstrap
}

do_install:append() {
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${UNPACKDIR}/${PN}.conf ${D}${sysconfdir}/dbus-1/system.d/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/${PN}.service ${D}${systemd_unitdir}/system/

    # Run by mmsd.service before mmsd to move the store it kept as root.
    install -d ${D}${libexecdir}
    install -m 0755 ${UNPACKDIR}/mmsd-migrate-store ${D}${libexecdir}/
}
