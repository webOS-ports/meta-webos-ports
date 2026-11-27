# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library used to interact with Android's binder module."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=11fd710e50bd899453f380488715f133"

DEPENDS = "libgbinder glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libgbinder-radio.git;branch=master;protocol=https \
           file://0001-gbinder-radio-Add-support-for-IRadioConfig-1.3.-JB-6.patch \
           file://0002-gbinder-radio-Add-support-for-IRadio-1.6.-JB-63650.patch \
           file://0003-gbinder-radio-Hold-the-request-while-its-response-is-han.patch \
"

PV = "1.6.7"
PE = "1"
PR = "r2"
SRCREV = "4937906a3d5574333f285dd073a08ece028e9de1"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} "
LDFLAGS += "--sysroot=${RECIPE_SYSROOT} "

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}' "
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
