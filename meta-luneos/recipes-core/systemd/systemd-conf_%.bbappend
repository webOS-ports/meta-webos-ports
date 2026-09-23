# Use logind.conf with disabled key handling
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://shutdown-timeouts.conf"
SRC_URI += "file://logind.conf"
SRC_URI += "file://journald-size.conf"

do_install:append() {
    install -D -m0644 ${S}/shutdown-timeouts.conf \
        ${D}${systemd_unitdir}/system.conf.d/10-luneos-shutdown-timeouts.conf

    install -D -m0644 ${S}/logind.conf \
        ${D}${systemd_unitdir}/logind.conf.d/10-luneos-key-handling.conf

    # Cap the journal; see the file for the measurements behind it.
    install -D -m0644 ${S}/journald-size.conf \
        ${D}${systemd_unitdir}/journald.conf.d/10-luneos-journal-size.conf
}

# Bumped when the drop-ins shipped from here change, so installed images pick
# them up on upgrade: the base recipe's revision does not move for it.
PR = "r1"
