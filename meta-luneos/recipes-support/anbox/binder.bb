SUMMARY = "Android IPC Subsystem"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://binder.c;endline=16;md5=b2770a40135de69c87b8e47553341bb7"

SRCREV = "27fd47e11ef6eef93738f8f3df3e42c88975544e"
PV = "3.0+git${SRCPV}"

inherit module

SRC_URI = "git://github.com/anbox/anbox-modules \
    file://0001-ashmem-binder-add-modules_install-target-to-Makefile.patch;patchdir=.. \
"

S = "${WORKDIR}/git/binder"

do_install_append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    grep binder ${S}/../99-anbox.rules > ${D}${sysconfdir}/udev/rules.d/99-binder.rules
}

KERNEL_MODULE_AUTOLOAD += "binder_linux"

FILES_${PN} += "${sysconfdir}/udev"
