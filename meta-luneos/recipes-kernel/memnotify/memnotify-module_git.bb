SECTION = "core"
SUMMARY = "System-wide memory meter and notifier pseudo-device"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://memnotify.c;beginline=1;endline=15;md5=ccb461cd1a6a487a14850dd387a794a4"

DEPENDS = "virtual/kernel"

inherit module

PV = "1.0.1+git"
SRCREV = "5592505e73380efb6e728140572cc048683ba768"

SRC_URI = "git://github.com/webOS-ports/memnotify-module.git;branch=master;protocol=https"
S = "${WORKDIR}/git"

do_install:append() {
    install -d ${D}${sysconfdir}/modules-load.d
    echo "memnotify" > ${D}${sysconfdir}/modules-load.d/memnotify.conf
}

FILES:${PN} += "${sysconfdir}/modules-load.d"
