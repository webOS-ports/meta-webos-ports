# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Implements libgrilio transport which allows to use binder IPC instead of RIL sockets."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://src/ril_binder_plugin.c;beginline=1;endline=31;md5=ba484c1af8c917210c60e63b7b499327"

DEPENDS = "ofono libgbinder libgbinder-radio libgrilio glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/ofono-ril-binder-plugin.git;branch=master;protocol=https"
S = "${WORKDIR}/git"

PV = "1.2.7"
SRCREV = "599c148fbd881015cc2be21b9d7e7554a30514fa"

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
