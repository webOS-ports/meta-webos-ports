# Copyright (c) 2012-2013 LG Electronics, Inc.

# Define the desired resolution and write it into the fbsetup script
UVESA_MODE = "1024x768-32"

do_install_append() {
    # Install module loading configuration for systemd-modules-load service
    install -d ${D}${sysconfdir}/modules-load.d
    echo "uvesafb" > ${D}${sysconfdir}/modules-load.d/v86d.conf
    install -d ${D}${sysconfdir}/modprobe.d
    echo "options uvesafb mode_option=${UVESA_MODE}" > ${D}${sysconfdir}/modprobe.d/uvesafb.conf
}
