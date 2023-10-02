# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library of glib utilities."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://src/gutil_log.c;beginline=1;endline=31;md5=40385e1b2a8460ac86b9d4b2893c5c3f"

DEPENDS = "glib-2.0"

inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/libglibutil.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.0.61-1+git"
SRCREV = "508e1255782e59725c25068874337e6309da04b2"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
