# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "Helper library for writing NCI based nfcd adaptation plugins."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=98cc3a1c8b4816ed2d8984676c8edaa8"

DEPENDS = "glib-2.0 libglibutil libncicore nfcd"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libnciplugin.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.2.2"
SRCREV = "0b543f1aa1f2d88fb14c38c15c8a6beb6ce13b0f"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 LIBDIR=${libdir}"
PARALLEL_MAKE = ""

do_compile() {
    oe_runmake release pkgconfig
}

do_install() {
    oe_runmake install-dev DESTDIR=${D}
}
