# Copyright (c) 2012-2013 LG Electronics, Inc.

# Define the desired resolution
UVESA_MODE = "1024x768-32"

do_install_append() {
    # Install modprobe configuration
    install -d ${D}${sysconfdir}/modprobe.d
    echo "options uvesafb mode_option=${UVESA_MODE}" > ${D}${sysconfdir}/modprobe.d/uvesafb.conf
}
