# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "Binder based NCI I/O plugin for nfcd (needed for Android 8.0+)."
LICENSE = "BSD-3-Clause"
SECTION = "webos/connectivity"
LIC_FILES_CHKSUM = "file://LICENSE;md5=284d1896a44bdc823c92ea2651ae31c6"

DEPENDS = "glib-2.0 libglibutil libgbinder libncicore libnciplugin nfcd"

inherit pkgconfig

# Talks to the Android NFC HAL over binder
COMPATIBLE_MACHINE = "^halium$"

SRC_URI = "git://github.com/mer-hybris/nfcd-binder-plugin.git;protocol=https;branch=master"

PV = "1.2.1"
SRCREV = "25a67ba973dc118d14625c2d66f75ca0afaa1bfc"

# The plugin picks the HAL flavour at runtime: the HIDL backend speaks
# android.hardware.nfc@1.0 (Halium 9/11) and the AIDL backend speaks
# android.hardware.nfc.INfc (Halium 13), so one build covers both.
EXTRA_OEMAKE = "KEEP_SYMBOLS=1 PLUGIN_DIR=${libdir}/nfcd/plugins"
PARALLEL_MAKE = ""

do_compile() {
    oe_runmake release
}

do_install() {
    oe_runmake install DESTDIR=${D}
}

FILES:${PN} += "${libdir}/nfcd/plugins/*.so"

RDEPENDS:${PN} += "nfcd"
