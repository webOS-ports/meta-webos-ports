SECTION = "core"
SUMMARY = "System-wide memory meter and notifier pseudo-device"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://memnotify.c;beginline=1;endline=15;md5=ccb461cd1a6a487a14850dd387a794a4"

DEPENDS = "virtual/kernel"

inherit module

PV = "1.0.0+gitr${SRCPV}"
SRCREV = "4d52505f5912962940c9df9b52ef7217932cf0ea"

SRC_URI = "git://github.com/webOS-ports/memnotify-module.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${sysconfdir}/modules-load.d
    echo "memnotify" > ${D}${sysconfdir}/modules-load.d/memnotify.conf
}
