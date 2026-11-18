# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library of glib utilities."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84b6ba729d0490a306a608778fb69982"

DEPENDS = "glib-2.0"

inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/libglibutil.git;protocol=https;branch=master"

PV = "1.0.83"
SRCREV = "fa82adf84ba308626c4389c6e5b95bb4c12ce1d6"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
