# Use logind.conf with disabled key handling
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://shutdown-timeouts.conf"
SRC_URI += "file://logind.conf"
SRC_URI += "file://journald-size.conf"
SRC_URI += "file://luneos-hardening.conf"
SRC_URI += "file://serial-getty-uart-present.conf"
SRC_URI += "file://serial-uart-present"

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
    # No getty on a serial port without a UART (console=ttyS0 on VirtualBox
    # without a serial port); see serial-uart-present.
    install -D -m0755 ${S}/serial-uart-present ${D}${libexecdir}/serial-uart-present
    install -D -m0644 ${S}/serial-getty-uart-present.conf \
        ${D}${systemd_system_unitdir}/serial-getty@.service.d/10-luneos-uart-present.conf
    sed -i -e 's,@LIBEXECDIR@,${libexecdir},g' \
        ${D}${systemd_system_unitdir}/serial-getty@.service.d/10-luneos-uart-present.conf
}

FILES:${PN} += " \
    ${libexecdir}/serial-uart-present \
    ${systemd_system_unitdir}/serial-getty@.service.d/ \
"

# Halium: keep PIDs within 32-bit bionic's limit; see the file.
SRC_URI:append:halium = " file://halium-pid-max.conf"

do_install:append:halium() {
    install -D -m0644 ${S}/halium-pid-max.conf \
        ${D}${nonarch_libdir}/sysctl.d/60-luneos-halium-pid-max.conf
}

FILES:${PN} += "${nonarch_libdir}/sysctl.d/"

# Bumped when the drop-ins shipped from here change, so installed images pick
# them up on upgrade: the base recipe's revision does not move for it.
PR = "r6"
