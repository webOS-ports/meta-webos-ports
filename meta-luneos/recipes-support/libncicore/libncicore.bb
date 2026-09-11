# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "NFC Controller Interface (NCI) core library, used by nfcd adaptation plugins."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4db8689062a4409f93d9ba042c1251d2"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libncicore.git;protocol=https;branch=master"

# No per-machine /etc/libncicore.conf here any more. sargo needed one - its
# PN81T rejects RF_DISCOVER when the technology set includes NFC-F - but sargo
# builds no rootfs of its own, so a machine-scoped file could never reach the
# image it runs. It is a Tier 2 adaptation instead, overlaid at runtime from
# luneos-device-config's adaptations/sargo/sparse/etc/libncicore.conf. The
# library falls back to its own defaults when the file is absent, which is what
# every other device gets.

PV = "1.1.33"
SRCREV = "aa9ccbb1148e49852e56df2241ecea15f80861a1"

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}' KEEP_SYMBOLS=1 LIBDIR=${libdir}"
PARALLEL_MAKE = ""

do_compile() {
    oe_runmake release pkgconfig
}

do_install() {
    oe_runmake install-dev DESTDIR=${D}
}
