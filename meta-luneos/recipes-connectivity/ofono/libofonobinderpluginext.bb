# Copyright (c) 2023 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "Extension framework for ofono binder plugin"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=37fe900f9ece53e2621d89780f2031be"
SECTION = "webos/support"

DEPENDS = "glib-2.0 libglibutil libgbinder-radio"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/ofono-binder-plugin.git;branch=master;protocol=https"
S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/lib"

PV = "1.1.28"
SRCREV = "7e9d3d57a46e1ad62b6082203d6c0f62554fe08c"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} "
LDFLAGS += "--sysroot=${RECIPE_SYSROOT} "

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}'"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
}

