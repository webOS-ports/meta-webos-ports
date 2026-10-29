# Use logind.conf with disabled key handling
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://shutdown-timeouts.conf"
SRC_URI += "file://logind.conf"
SRC_URI += "file://journald-size.conf"
SRC_URI += "file://luneos-hardening.conf"

do_install:append() {
    install -D -m0644 ${S}/shutdown-timeouts.conf \
        ${D}${systemd_unitdir}/system.conf.d/10-luneos-shutdown-timeouts.conf

    install -D -m0644 ${S}/logind.conf \
        ${D}${systemd_unitdir}/logind.conf.d/10-luneos-key-handling.conf

    # Cap the journal; see the file for the measurements behind it.
    install -D -m0644 ${S}/journald-size.conf \
        ${D}${systemd_unitdir}/journald.conf.d/10-luneos-journal-size.conf

    # Kernel hardening sysctls; see the file for what was checked.
    install -D -m0644 ${S}/luneos-hardening.conf \
        ${D}${nonarch_libdir}/sysctl.d/50-luneos-hardening.conf
}

# Halium: keep PIDs within 32-bit bionic's limit; see the file.
SRC_URI:append:halium = " file://halium-pid-max.conf"

do_install:append:halium() {
    install -D -m0644 ${S}/halium-pid-max.conf \
        ${D}${nonarch_libdir}/sysctl.d/60-luneos-halium-pid-max.conf
}

FILES:${PN} += "${nonarch_libdir}/sysctl.d/"

# Bumped when the drop-ins shipped from here change, so installed images pick
# them up on upgrade: the base recipe's revision does not move for it.
PR = "r3"
