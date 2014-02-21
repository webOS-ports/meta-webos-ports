# Copyright (c) 2012-2013 LG Electronics, Inc.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Override default fbsetup script with our own script which sets the screen resolution
SRC_URI += "file://fbsetup"

# Define the desired resolution and write it into the fbsetup script
UVESA_MODE = "1024x768-32"

do_configure_prepend() {
    sed -i.orig "s/modprobe uvesafb.*$/modprobe uvesafb mode_option=${UVESA_MODE}/" ${WORKDIR}/fbsetup
}
