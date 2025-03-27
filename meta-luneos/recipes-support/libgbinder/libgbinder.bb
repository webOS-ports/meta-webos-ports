# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library used to interact with Android's binder module."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=78995ef51510572817bf9586588261b3"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libgbinder.git;branch=master;protocol=https \
           file://gbinder.conf \
"
S = "${WORKDIR}/git"

PV = "1.1.35"
SRCREV = "e3f705c4cc6b820d8885b565fc7995e02dd196b3"

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

#     src/gbinder_writer.c:1318:60: error: passing argument 2 of 'gbinder_cleanup_add' from incompatible pointer type [-Wincompatible-pointer-types]
#     src/gbinder_writer.c:1329:55: error: passing argument 4 of 'gbinder_writer_alloc' from incompatible pointer type [-Wincompatible-pointer-types]
#     src/gbinder_writer.c:1337:56: error: passing argument 4 of 'gbinder_writer_alloc' from incompatible pointer type [-Wincompatible-pointer-types]
CFLAGS += "-std=gnu17"
