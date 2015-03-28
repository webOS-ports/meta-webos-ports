do_install_append() {
    # Make sure bluetooth.service gets started on boot
    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
    ln -sf /lib/systemd/system/bluetooth.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants
}
