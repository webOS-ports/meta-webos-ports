# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "Library for parsing and building NDEF messages."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=df51399d0baac2e00f462b7f8acc0812"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/libnfcdef.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.1.0"
SRCREV = "2f4d115c977919de74fbf1ab30ce8d43b1d7ed32"

# The upstream Makefile derives its lib dir from pkg-config, which resolves to
# the host's when cross compiling, so pass it explicitly.
EXTRA_OEMAKE = "KEEP_SYMBOLS=1 LIBDIR=${libdir}"
PARALLEL_MAKE = ""

do_compile() {
    oe_runmake release
}

do_install() {
    oe_runmake install-dev DESTDIR=${D}
}
