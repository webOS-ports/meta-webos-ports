SUMMARY = "Android IPC Subsystem"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://binder.c;endline=16;md5=b2770a40135de69c87b8e47553341bb7"

SRCREV = "4a5cb84231bcff0e3ebe44f6dcd6accf8bcf2e92"
PV = "3.0+git${SRCPV}"

inherit module

SRC_URI = "git://github.com/anbox/anbox \
    file://0001_adapt_Makefile.patch \
"

S = "${WORKDIR}/git/kernel/binder"

KERNEL_MODULE_AUTOLOAD += "binder_linux"
