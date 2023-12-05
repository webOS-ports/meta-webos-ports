# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library of glib utilities."
LICENSE = "MIT"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://src/grilio_log.h;beginline=1;endline=31;md5=093251e68396d04fb55cb68b9bfdeb18"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/libgrilio.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.0.44-1+git${SRCPV}"
SRCREV = "014c4cdd0feb17c3d723dde90dbd9ad7376798dc"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
