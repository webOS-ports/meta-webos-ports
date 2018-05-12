SUMMARY = "Anonymous Shared Memory Subsystem, ashmem"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://ashmem.c;endline=17;md5=c74d8e70552b07bbd79f07120b792292"

require anbox.inc

inherit module

SRC_URI += "file://0001_adapt_Makefile.patch"

S = "${WORKDIR}/git/kernel/ashmem"

do_install_append() {
    install -d ${D}${sysconfdir}/modules-load.d
    echo "ashmem_linux" > ${D}${sysconfdir}/modules-load.d/ashmem.conf
}

FILES_${PN} += " ${sysconfdir}/modules-load.d"
