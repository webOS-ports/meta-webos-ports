# Use logind.conf with disabled key handling
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://logind.conf"

do_install:append() {
    # Keep logind's hands off the hardware keys.
    #
    # logind watches the gpio-keys input node on every device that has one
    # ("systemd-logind: Watching system buttons on /dev/input/event4"), and
    # with HandlePowerKey unset it applies systemd's default of poweroff. So a
    # press of the power button shut the phone down instead of blanking the
    # screen - intermittently, because the shell reads the same node through
    # nyx and whichever side won the race decided what happened. On a
    # PinePhone Pro that produced a run of "spontaneous reboots" that looked
    # like suspend crashes and were not: the journal showed a clean shutdown
    # sequence every time, with the device awake and on the charger.
    #
    # The shell owns the power, volume and lid keys; logind must ignore them.
    # This file has been sitting in the recipe directory unused, which is why
    # the first line of this bbappend already claimed key handling was
    # disabled.
    install -D -m0644 ${S}/logind.conf \
        ${D}${systemd_unitdir}/logind.conf.d/10-luneos-key-handling.conf
}
