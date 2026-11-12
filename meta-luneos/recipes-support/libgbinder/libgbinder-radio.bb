# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library used to interact with Android's binder module."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=11fd710e50bd899453f380488715f133"

DEPENDS = "libgbinder glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libgbinder-radio.git;branch=master;protocol=https \
           file://0001-gbinder-radio-Update-the-timer-when-a-request-is-que.patch \
           file://0002-gbinder-radio-Take-a-retried-request-out-of-the-queu.patch \
           file://0003-gbinder-radio-Let-the-next-group-become-the-owner-wh.patch \
           file://0004-gbinder-radio-Start-over-after-running-callbacks-whi.patch \
           file://0005-gbinder-radio-Push-the-queue-when-a-waiting-group-st.patch \
           file://0006-gbinder-radio-Hold-a-reference-while-dispatching-res.patch \
           file://0007-gbinder-radio-Keep-the-client-alive-while-it-handles.patch \
           file://0008-gbinder-radio-Don-t-let-a-dead-instance-unregister-i.patch \
           file://0009-gbinder-radio-Don-t-call-functions-through-mismatche.patch \
"

PV = "1.6.6+git"
SRCREV = "7ecf42f4c56e406406fb525efd6610fc1ac143b7"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} "
LDFLAGS += "--sysroot=${RECIPE_SYSROOT} "

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}' "
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
