FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Sandbox for cupsd and everything it starts; see the file.
SRC_URI += "file://cups.service.d/luneos-hardening.conf"

do_install:append() {
    install -D -m 0644 ${UNPACKDIR}/cups.service.d/luneos-hardening.conf \
        ${D}${systemd_system_unitdir}/cups.service.d/luneos-hardening.conf
}

FILES:${PN} += "${systemd_system_unitdir}/cups.service.d"
