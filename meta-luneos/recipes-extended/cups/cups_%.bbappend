FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Sandbox for cupsd and everything it starts; see the file.
SRC_URI += "file://cups.service.d/luneos-hardening.conf"

do_install:append() {
    install -D -m 0644 ${UNPACKDIR}/cups.service.d/luneos-hardening.conf \
        ${D}${systemd_system_unitdir}/cups.service.d/luneos-hardening.conf
}

# Do not enable cups-lpd.socket. It accepts unauthenticated LPD print jobs
# on TCP 515 from any host into the local queues; printing from the phone
# goes through cupsd/IPP and never needs it. The units stay installed so it
# can still be enabled by hand.
SYSTEMD_SERVICE:${PN}:remove = "cups-lpd.socket"

FILES:${PN} += " \
    ${systemd_system_unitdir}/cups.service.d \
    ${systemd_system_unitdir}/cups-lpd.socket \
    ${systemd_system_unitdir}/cups-lpd@.service \
"
