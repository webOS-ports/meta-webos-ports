SUMMARY = "Anonymous Shared Memory Subsystem, ashmem"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://ashmem.c;endline=17;md5=c74d8e70552b07bbd79f07120b792292"

SRCREV = "4a5cb84231bcff0e3ebe44f6dcd6accf8bcf2e92"
PV = "3.0+git${SRCPV}"

inherit module

SRC_URI = "git://github.com/anbox/anbox \
    file://0001_adapt_Makefile.patch \
"

S = "${WORKDIR}/git/kernel/ashmem"

KERNEL_MODULE_AUTOLOAD += "ashmem_linux"
