# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Library used to interact with Android's binder module."
LICENSE = "BSD-3-Clause"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6b4103b77e6fa766a75a1c2c3ba715c8"

DEPENDS = "glib-2.0 libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/libgbinder.git;branch=master;protocol=https \
           file://gbinder.conf \
"

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

# Install libgbinder's config for Halium 9.0, we do this here, since for Waydroid we need a different API version it seems, so better to split it for mainline targets such as PinePhone and qemux86-64.
do_install:append:halium() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${UNPACKDIR}/gbinder.conf ${D}${sysconfdir}/gbinder.conf
}

FILES:${PN} += " ${sysconfdir}"

#     src/gbinder_writer.c:1318:60: error: passing argument 2 of 'gbinder_cleanup_add' from incompatible pointer type [-Wincompatible-pointer-types]
#     src/gbinder_writer.c:1329:55: error: passing argument 4 of 'gbinder_writer_alloc' from incompatible pointer type [-Wincompatible-pointer-types]
#     src/gbinder_writer.c:1337:56: error: passing argument 4 of 'gbinder_writer_alloc' from incompatible pointer type [-Wincompatible-pointer-types]
CFLAGS += "-std=gnu17"
