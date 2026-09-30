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
    # binder-ping and binder-list from tools/, packaged as libgbinder-tools.
    # ping tests one name, list enumerates a node; both replace lshal, which
    # SIGSEGVs before the container is up and returns nothing on some devices.
    # 40-ofono-binder uses list to find the modem's IRadio/slotN topology.
    for t in binder-ping binder-list; do
        ${CC} ${CFLAGS} ${LDFLAGS} \
            ${S}/tools/$t/$t.c \
            -o ${B}/$t \
            -I${S}/include \
            `pkg-config --cflags glib-2.0 gio-2.0 gio-unix-2.0 libglibutil` \
            -L${B}/build/release -lgbinder \
            `pkg-config --libs glib-2.0 gio-2.0 gio-unix-2.0 libglibutil`
    done
}

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
    install -D -m 0755 ${B}/binder-ping ${D}${bindir}/binder-ping
    install -D -m 0755 ${B}/binder-list ${D}${bindir}/binder-list
}

PACKAGES =+ "libgbinder-tools"
FILES:libgbinder-tools = "${bindir}/binder-ping ${bindir}/binder-list"
RDEPENDS:libgbinder-tools = "libgbinder"

# Fallback only. gbinder picks a protocol preset from an API level; the level
# belongs to the vendor the device boots against, which is only knowable at
# runtime, so luneos-device-config writes /etc/gbinder.d/10-luneos-device.conf
# from the vendor properties and that overrides this file. Per-machine values
# used to live here and could never take effect - halium-arm64's rootfs is
# shared by devices with different vendor SDKs, so no baked-in number is right.
# This is used only where there is no Android container to read properties from;
# 30 is mid-range of the presets (28, 29, 30, 31, 33, 35, 36).
#
# PACKAGE_ARCH: machines sharing TUNE_PKGARCH would otherwise collide in sstate.
GBINDER_API_LEVEL ?= "30"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install:append() {
    install -d ${D}${sysconfdir}
    printf '[General]\nApiLevel = %s\n' "${GBINDER_API_LEVEL}" > ${D}${sysconfdir}/gbinder.conf
}

FILES:${PN} += " ${sysconfdir}"

# gnu17: gbinder_writer.c passes incompatible pointer types, fatal under gnu23.
CFLAGS += "-std=gnu17"
