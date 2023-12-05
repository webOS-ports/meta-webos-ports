# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library of glib utilities."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d06f24f367f70ea228818e7442fa90c7"

DEPENDS = "glib-2.0"

inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/libglibutil.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.0.75-1+git${SRCPV}"
SRCREV = "4e110017fd4f852a3b1e5616baf111813be9fe92"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
