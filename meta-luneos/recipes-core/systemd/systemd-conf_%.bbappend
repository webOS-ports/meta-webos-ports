# Use logind.conf with disabled key handling
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://shutdown-timeouts.conf"
SRC_URI += "file://logind.conf"
SRC_URI += "file://journald-size.conf"

# A separate drop-in rather than an override of the recipe's own system.conf:
# upstream installs 00-, the qemuall override installs 01-, and this only needs
# to land after both.
do_install:append() {
    install -D -m0644 ${S}/shutdown-timeouts.conf \
        ${D}${systemd_unitdir}/system.conf.d/10-luneos-shutdown-timeouts.conf

    # Keep logind's hands off the hardware keys. It watches the gpio-keys node
    # and with HandlePowerKey unset applies systemd's default of poweroff, so
    # the power button shut the device down instead of blanking the screen -
    # intermittently, because the shell reads the same node through nyx. This
    # file sat in the recipe directory unused, which is why line 1 already
    # claimed key handling was disabled.
    install -D -m0644 ${S}/logind.conf \
        ${D}${systemd_unitdir}/logind.conf.d/10-luneos-key-handling.conf

    # Cap the journal; see the file for the measurements behind it.
    install -D -m0644 ${S}/journald-size.conf \
        ${D}${systemd_unitdir}/journald.conf.d/10-luneos-journal-size.conf
}

# Bumped when the drop-ins shipped from here change, so installed images pick
# them up on upgrade: the base recipe's revision does not move for it.
PR = "r1"
