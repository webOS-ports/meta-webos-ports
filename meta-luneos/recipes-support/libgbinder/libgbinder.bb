# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library used to interact with Android's binder module."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6b4103b77e6fa766a75a1c2c3ba715c8"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libgbinder.git;branch=master;protocol=https"

PV = "1.1.52"
SRCREV = "e906afcffbfa51b7fbefe042a13b933d9e8dfdd9"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"
PARALLEL_MAKE = ""

do_compile:append() {
    # Build binder-ping from tools/ against the just-built library. It is
    # packaged separately (libgbinder-tools) and used by the Android-container
    # readiness probe to wait for a HIDL service on /dev/hwbinder, replacing the
    # crash-prone lshal: binder-ping returns a clean exit code and does not
    # SIGSEGV when run before the container's linker/hwservicemanager are ready.
    ${CC} ${CFLAGS} ${LDFLAGS} \
        ${S}/tools/binder-ping/binder-ping.c \
        -o ${B}/binder-ping \
        -I${S}/include \
        `pkg-config --cflags glib-2.0 gio-2.0 gio-unix-2.0 libglibutil` \
        -L${B}/build/release -lgbinder \
        `pkg-config --libs glib-2.0 gio-2.0 gio-unix-2.0 libglibutil`
}

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
    install -D -m 0755 ${B}/binder-ping ${D}${bindir}/binder-ping
}

PACKAGES =+ "libgbinder-tools"
FILES:libgbinder-tools = "${bindir}/binder-ping"
RDEPENDS:libgbinder-tools = "libgbinder"

# gbinder picks the protocol presets for /dev/binder and /dev/vndbinder from an
# API level, and with nothing configured it assumes the oldest. That level is a
# property of the Android side the host talks to: on a Halium device the
# vendor's, which is ro.vndk.version, and on a device whose only Android is the
# Waydroid container, that image's.
#
# It used to be a static file saying 28, installed only on Halium machines,
# with waydroid.bb installing a near-identical one saying 30 on each of the
# others through four copies of the same do_install:append. One file, generated
# from one variable, replaces all of that - which is also why this recipe now
# needs PACKAGE_ARCH: tissot-halium, mido-halium and halium-arm64 share
# TUNE_PKGARCH, so machine-specific content under the tune arch would collide
# in sstate and in the feed.
#
# The presets only reach /dev/binder and /dev/vndbinder; /dev/hwbinder is not in
# them, and Waydroid passes explicit protocols for the container's own binder
# nodes, taken from the system image's SDK level. So this setting is about the
# host's own Android HALs, not about Waydroid.
GBINDER_API_LEVEL ?= "30"
GBINDER_API_LEVEL:tissot-halium = "28"
GBINDER_API_LEVEL:mido-halium = "28"
GBINDER_API_LEVEL:mindphone = "30"
GBINDER_API_LEVEL:halium-arm64 = "32"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install:append() {
    install -d ${D}${sysconfdir}
    printf '[General]\nApiLevel = %s\n' "${GBINDER_API_LEVEL}" > ${D}${sysconfdir}/gbinder.conf
}

FILES:${PN} += " ${sysconfdir}"

#     src/gbinder_writer.c:1318:60: error: passing argument 2 of 'gbinder_cleanup_add' from incompatible pointer type [-Wincompatible-pointer-types]
#     src/gbinder_writer.c:1329:55: error: passing argument 4 of 'gbinder_writer_alloc' from incompatible pointer type [-Wincompatible-pointer-types]
#     src/gbinder_writer.c:1337:56: error: passing argument 4 of 'gbinder_writer_alloc' from incompatible pointer type [-Wincompatible-pointer-types]
CFLAGS += "-std=gnu17"
