do_install_append() {
    # Systemd searches for fsck.vfat at the wrong place so point it to the right one
    install -d ${D}${base_sbindir}
    ln -sf ${sbindir}/fsck.vfat ${D}${base_sbindir}/fsck.vfat
}
