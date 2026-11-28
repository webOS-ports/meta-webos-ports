# Copyright (c) 2023 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "Binder based ofono plugin (Needed for Android 8.0+)."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=79d356a113871df9a6301458b5de257a"
SECTION = "webos/support"

DEPENDS = "ofono ofono-ext libgbinder libgbinder-radio glib-2.0 libglibutil libofonobinderpluginext"
# LUNEOS=1: the display state comes from luna-displaymanager, not MCE
DEPENDS += "luna-service2 json-c"

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
           file://0011-ofono-binder-Don-t-keep-the-slot-blocked-for-a-card-.patch \
           file://0012-unit-Add-SIM-card-status-transaction-test.patch \
           file://0013-ofono-binder-Reset-the-SIM-card-on-request-by-poweri.patch \
           file://0014-unit-Test-the-SIM-card-power-cycle.patch \
           file://0015-ofono-binder-Report-a-present-card-without-applicati.patch \
           file://0016-unit-Test-when-the-SIM-driver-reports-a-SIM-inserted.patch \
           file://0017-unit-Test-the-SIM-driver-s-card-reset.patch \
           file://0018-ofono-binder-Ask-for-IMS-registration-when-the-IMS-a.patch \
           file://0019-ofono-binder-Give-an-IMS-extension-a-default-IMS-dat.patch \
           file://0020-ofono-binder-Add-support-for-RadioConfig-1.3-interfa.patch \
           file://0021-ofono-binder-Add-support-for-Radio-1.6-interface.-JB.patch \
           file://0022-ofono-binder-Read-the-card-s-applications-from-the-1.patch \
           file://0023-unit-Test-reading-the-1.5-card-status.patch \
           file://0024-ofono-binder-Use-the-1.6-voice-call-API-on-IRadio-1..patch \
           file://0025-unit-Test-which-API-the-voice-call-driver-uses.patch \
           file://0026-ofono-binder-Take-the-display-as-on-while-MCE-hasn-t.patch \
           file://0027-unit-Test-what-the-device-monitors-take-the-display-.patch \
           file://0028-ofono-binder-Send-setupDataCall_1_6-s-safe_unions-as.patch \
           file://0029-unit-Test-how-setupDataCall-s-arguments-are-written.patch \
           file://0030-ofono-binder-Keep-the-display-state-in-one-object-fo.patch \
           file://0031-ofono-binder-Take-the-display-state-from-luna-displa.patch \
           file://0032-unit-Test-the-luna-displaymanager-display-state.patch \
           file://0033-ofono-binder-Tell-the-modem-the-XCAP-access-point.patch \
           file://0034-unit-Test-the-APN-types-of-the-XCAP-data-profile.patch \
           file://0035-ofono-binder-Tell-oFono-which-calls-are-carried-over.patch \
           file://0036-unit-Test-which-calls-oFono-hears-are-IMS.patch \
           file://0037-ofono-binder-Let-IMS-and-XCAP-data-profiles-use-IWLA.patch \
           file://0038-unit-Test-the-bearers-of-the-IMS-and-XCAP-data-profi.patch \
           file://0039-ofono-binder-Keep-the-IMS-atom-while-the-modem-is-of.patch \
           file://0040-ofono-binder-Hang-up-a-dial-the-extension-hasn-t-lis.patch \
           file://0041-unit-Test-hanging-up-a-dial-the-extension-hasn-t-lis.patch \
"

PV = "1.1.41"
PR = "r10"
SRCREV = "204dea1593be95e60bad9f2c0282c35a9f8ba272"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} "
LDFLAGS += "--sysroot=${RECIPE_SYSROOT} "

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 LUNEOS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}'"
PARALLEL_MAKE = ""

do_install() {
    # With LUNEOS=1 this also installs ofonod's luna-service2 role,
    # permissions and manifest from LuneOS/sysbus.
    make install DESTDIR=${D} LUNEOS=1
}

FILES:${PN} += "/usr/lib/ofono/plugins/binderplugin.so ${datadir}/luna-service2"

RDEPENDS:${PN} += "ofono-ext ofono-ext-plugin"
