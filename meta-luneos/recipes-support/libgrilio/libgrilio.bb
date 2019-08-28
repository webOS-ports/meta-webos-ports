# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library of glib utilities."
LICENSE = "MIT"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://src/grilio_log.h;beginline=1;endline=31;md5=093251e68396d04fb55cb68b9bfdeb18"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://git.merproject.org/mer-core/libgrilio.git"
S = "${WORKDIR}/git"

PV = "1.0.36-1+git${SRCPV}"
SRCREV = "8a01e8093ec1bea57692aaad256e0a1dd59f0bf0"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
