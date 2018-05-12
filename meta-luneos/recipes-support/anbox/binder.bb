SUMMARY = "Android IPC Subsystem"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://binder.c;endline=16;md5=b2770a40135de69c87b8e47553341bb7"

require anbox.inc

inherit module

SRC_URI += "file://0001_adapt_Makefile.patch"

S = "${WORKDIR}/git/kernel/binder"

do_install_append() {
    install -d ${D}${sysconfdir}/modules-load.d
    echo "binder_linux" > ${D}${sysconfdir}/modules-load.d/binder.conf
}

FILES_${PN} += " ${sysconfdir}/modules-load.d"
