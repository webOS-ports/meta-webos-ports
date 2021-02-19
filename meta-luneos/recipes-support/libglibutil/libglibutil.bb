# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library of glib utilities."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://src/gutil_log.c;beginline=1;endline=31;md5=c476c5938ec00208b29c1c1743b4a006"

DEPENDS = "glib-2.0"

inherit pkgconfig

SRC_URI = "git://git.merproject.org/mer-core/libglibutil.git"
S = "${WORKDIR}/git"

PV = "1.0.50-1+git${SRCPV}"
SRCREV = "dfcf23805b49307e63d1090aec69255de4a167ea"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
