# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library of glib utilities."
LICENSE = "MIT"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://src/grilio_log.h;beginline=1;endline=31;md5=093251e68396d04fb55cb68b9bfdeb18"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/libgrilio.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.0.39-1+git${SRCPV}"
SRCREV = "554f3b65715e920c4fe7c767a461ef1f4eb96f0c"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
