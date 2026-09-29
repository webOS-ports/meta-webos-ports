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
    # binder-list too: it ENUMERATES the services on a binder node, which
    # binder-ping cannot - ping answers "is this one name alive", list answers
    # "what is registered". luneos-device-config's 40-ofono-binder needs the
    # latter to discover the modem's IRadio/slotN topology, and its only source
    # for that was lshal, which returns nothing at all on some devices (radon's
    # MT6877; sargo dies in getDeviceHalManifest with a VINTF parse error). On
    # radon that left the shipped single-slot binder.conf in place and the phone
    # showed one SIM on dual-SIM hardware. Verified by hand there:
    #   binder-list -d /dev/hwbinder | grep IRadio/slot
    #   -> android.hardware.radio@1.0..1.6::IRadio/slot1 and /slot2
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
# ONE value, not a per-machine table. This is only a fallback.
#
# The level must match the VENDOR the device boots against, not the device's
# age and not the GSI generation - the runtime source is ro.board.api_level,
# then ro.vndk.version, then ro.build.version.sdk, all of which come off the
# vendor partition. Moving a machine from a 9.0 GSI to the 16.0 one does not
# change it.
#
# But it is written at runtime anyway. luneos-device-config reads those same
# vendor properties and writes /etc/gbinder.d/10-luneos-device.conf, and files
# in /etc/gbinder.d override /etc/gbinder.conf (gbinder_config.c says so in as
# many words). Its unit is After=android-system.service, so the properties
# exist by then, and Before= every consumer there is - surface-manager,
# sensorfwd, ofono, configd, nfcd, pulseaudio, bluebinder, nyx.target. So the
# generated file is always in place before anything can read the packaged one.
#
# That is why the per-machine overrides are gone. They were
#
#   tissot-halium 28   mido-halium 28   athena 35
#   mindphone 30       halium-arm  30   halium-arm64 32
#
# and every one of them was either inert or actively misleading:
#
#   - mindphone and halium-arm were identical to this default, so no-ops.
#   - tissot-halium, mido-halium and athena were correct for their vendors but
#     superseded on every boot, so editing them could never change anything.
#   - halium-arm64 = 32 was the actual trap. That rootfs is SHARED by sargo,
#     sunfish, bramble and bluejay, whose vendors are not all SDK 32 (sargo is
#     32, a LineageOS 23.2 vendor is 36). One value baked into one image cannot
#     be right for all of them, and the runtime file is what saves them.
#
# Keeping a table that cannot take effect invites someone to "fix" a device by
# editing a number here and conclude the stack is broken when nothing changes.
#
# This value still matters in exactly one case: a machine with no Android
# container at all, where luneos-device-config finds no vendor properties, logs
# "no vendor api_level found, leaving gbinder at the packaged default", and this
# is what gbinder uses. 30 is the sane middle of the available presets (28, 29,
# 30, 31, 33, 35, 36; the highest <= ApiLevel wins).
#
# To check what a device actually ended up with, read it off the device rather
# than the metadata:
#
#   cat /etc/gbinder.d/10-luneos-device.conf   # what the runtime chose
#   cat /etc/gbinder.conf                      # this fallback
#
# See luneos-device-config and hal-userspace.md.
GBINDER_API_LEVEL ?= "30"

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
