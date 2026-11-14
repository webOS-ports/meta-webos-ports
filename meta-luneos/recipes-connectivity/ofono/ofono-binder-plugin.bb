# Copyright (c) 2023 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "Binder based ofono plugin (Needed for Android 8.0+)."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=79d356a113871df9a6301458b5de257a"
SECTION = "webos/support"

DEPENDS = "ofono ofono-ext libgbinder libgbinder-radio glib-2.0 libglibutil libofonobinderpluginext"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/ofono-binder-plugin.git;branch=master;protocol=https \
           file://0001-binder_connman-do-not-read-non-basic-property-varian.patch \
           file://0002-ofono-binder-Return-SW1-SW2-with-logical-channel-APD.patch \
           file://0003-ofono-binder-Implement-set_active_card_slot-with-set.patch \
           file://0004-Build-against-upstream-oFono-2.x-plus-ofono-ext.patch \
           file://0005-binder_voicecall-do-not-poll-calls-during-a-call-con.patch \
           file://0006-unit-test-when-binder_voicecall-asks-for-the-call-li.patch \
           file://0007-binder_call_barring-don-t-keep-asking-a-modem-which-.patch \
           file://0008-ofono-binder-Don-t-make-supplementary-service-reques.patch \
           file://0009-unit-test-the-call-settings-requests.patch \
           file://0010-unit-free-failed-voicecall-requests-the-way-libgbind.patch \
           file://0011-ofono-binder-Don-t-let-a-failed-submit-stall-the-rad.patch \
           file://0012-unit-Add-radio-caps-transaction-test.patch \
           file://0013-ofono-binder-Send-radio-caps-data-requests-in-the-tr.patch \
           file://0014-ofono-binder-Switch-the-radios-back-on-after-a-radio.patch \
           file://0015-ofono-binder-Don-t-keep-the-slot-blocked-for-a-card-.patch \
           file://0016-unit-Add-SIM-card-status-transaction-test.patch \
"

PV = "1.1.39"
PR = "r2"
SRCREV = "67c3c85c58fb387d1303560f757144ed275c67d6"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} "
LDFLAGS += "--sysroot=${RECIPE_SYSROOT} "

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}'"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
}

FILES:${PN} += "/usr/lib/ofono/plugins/binderplugin.so"

RDEPENDS:${PN} += "ofono-ext ofono-ext-plugin"
