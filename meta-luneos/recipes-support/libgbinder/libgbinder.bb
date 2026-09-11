# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library used to interact with Android's binder module."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
# Checksum updated with the version bump below. Verified by reading the file:
# still BSD-3-Clause, only the copyright holders/years changed.
LIC_FILES_CHKSUM = "file://LICENSE;md5=6b4103b77e6fa766a75a1c2c3ba715c8"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libgbinder.git;branch=master;protocol=https \
           file://gbinder.conf \
"
S = "${WORKDIR}/git"

# Bumped 1.1.35 -> 1.1.52 to MATCH WHAT THE DEVICE ALREADY SHIPS. The prebuilt
# LuneOS rootfs on bluejay carries libgbinder.so.1.1.52, so this recipe was the
# stale side of the divergence, not the device.
#
# Required by the bluebinder SRCREV bump: bluebinder's AIDL path calls
# gbinder_local_object_set_stability() with GBINDER_STABILITY_VINTF, and both
# only became public API in 1.1.40 (GBINDER_STABILITY_LEVEL is tagged
# "Since 1.1.40" in include/gbinder_types.h). Against 1.1.35 the build fails
# with "'GBINDER_STABILITY_VINTF' undeclared".
#
# Matching the deployed version also means ofono's binderplugin.so and
# libgbinder-radio keep running against the exact library they were built for.
PV = "1.1.52"
SRCREV = "e906afcffbfa51b7fbefe042a13b933d9e8dfdd9"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}

# Install libgbinder's config for Halium 9.0, we do this here, since for Waydroid we need a different API version it seems, so better to split it for mainline targets such as PinePhone and qemux86-64.
do_install:append:halium() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/gbinder.conf ${D}${sysconfdir}/gbinder.conf
}

FILES:${PN} += " ${sysconfdir}"
