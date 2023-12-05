# Copyright (c) 2023 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "Binder based ofono plugin (Needed for Android 9.0+)."
LICENSE = "GNUPL"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://COPYING;md5=ba484c1af8c917210c60e63b7b499327"

DEPENDS = "ofono libgbinder libgbinder-radio glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/ofono-binder-plugin.git;branch=master;protocol=https"
S = "${WORKDIR}/git"

PV = "1.1.12"
SRCREV = "0bd4932f0c30187cd90ac91fe40c9e7131ed6110"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} "
LDFLAGS += "--sysroot=${RECIPE_SYSROOT} "

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}'"
PARALLEL_MAKE = ""

do_install() {
    make pkgconfig
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}

FILES:${PN} += "/usr/lib/ofono/plugins/rilbinderplugin.so"
