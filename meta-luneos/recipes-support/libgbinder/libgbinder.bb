# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library used to interact with Android's binder module."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6b4103b77e6fa766a75a1c2c3ba715c8"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libgbinder.git;branch=master;protocol=https \
           file://gbinder.conf \
"
S = "${WORKDIR}/git"

PV = "1.1.52"
SRCREV = "e906afcffbfa51b7fbefe042a13b933d9e8dfdd9"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}

# Install libgbinder's config for Halium 9.0, we do this here, since for Waydroid we need a different API version it seems, so better to split it for mainline targets such as PinePhone and qemux86-64.
do_install:append:halium() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/gbinder.conf ${D}${sysconfdir}/gbinder.conf
}

FILES:${PN} += " ${sysconfdir}"
