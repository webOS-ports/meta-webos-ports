SUMMARY = "Anonymous Shared Memory Subsystem, ashmem"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://ashmem.c;endline=17;md5=c74d8e70552b07bbd79f07120b792292"

SRCREV = "27fd47e11ef6eef93738f8f3df3e42c88975544e"
PV = "3.0+git"

inherit module

SRC_URI = "git://github.com/anbox/anbox-modules;branch=master;protocol=https \
    file://0001-ashmem-binder-add-modules_install-target-to-Makefile.patch;patchdir=.. \
"

S = "${UNPACKDIR}/git/ashmem"

do_install:append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    grep ashmem ${S}/../99-anbox.rules > ${D}${sysconfdir}/udev/rules.d/99-ashmem.rules
}

KERNEL_MODULE_AUTOLOAD += "ashmem_linux"

FILES:kernel-module-ashmem-linux += "${sysconfdir}/udev"

# Needs quite new kernel (probably >= 3.18) and from LuneOS supported machines
# only qemux86, qemux86-64 and rpi (later hammerhead-mainline) MACHINEs have it
COMPATIBLE_MACHINE ?= "(^$)"
COMPATIBLE_MACHINE:qemux86 = "(.*)"
COMPATIBLE_MACHINE:qemux86-64 = "(.*)"
COMPATIBLE_MACHINE:rpi = "(.*)"
