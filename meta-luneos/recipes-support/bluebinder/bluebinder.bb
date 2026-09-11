# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Simple proxy for using android binder based bluetooth through vhci."
LICENSE = "GPL-2.0-or-later"
SECTION = "webos/support"
# Checksum updated with the SRCREV bump below. Verified by reading the header:
# the licence is still GPL-2.0-or-later, only the Jolla copyright year range
# changed (2018-2019 -> 2018-2022).
LIC_FILES_CHKSUM = "file://bluebinder.c;beginline=1;endline=27;md5=ba7fb591c7626c434dcc691ce7797fd1"

DEPENDS = "libgbinder glib-2.0 libglibutil bluez5 systemd"
RDEPENDS:${PN} = "android-property-service"

# Rdepends on android-property-service which depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

# Rdepends on android-property-service which is MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig
inherit systemd

SRC_URI = "git://github.com/mer-hybris/bluebinder.git;branch=master;protocol=https \
           file://0001-Adapt-service-and-HAL-wait-for-LuneOS.patch \
           file://0003-Do-not-close-a-HAL-we-never-opened.patch \
"
SRC_URI:append:tissot-halium = " file://0002-service-load-after-wifi-module-load.patch"

S = "${WORKDIR}/git"

# Bumped from the 2019-era 419ab4a, which spoke ONLY the HIDL
# android.hardware.bluetooth@1.0::IBluetoothHci. Recent Android devices such
# as bluejay (Pixel 6a, real vendor confirmed Android 16 via getprop) publish
# the Bluetooth HAL as AIDL only -- their VINTF
# manifest has <hal format="aidl"> android.hardware.bluetooth / IBluetoothHci
# and no @1.0 HIDL version at all -- so the old revision could never bind.
# Upstream added AIDL support in fecaa79 ("bluebinder: add support for AIDL
# Bluetooth HAL"), with follow-ups for init status (e166428), ISO data
# (17da3c7) and rfkill power recovery (3794a84). This pin includes all of them.
#
# The old 0001-Use-CC-as-compiler.patch is dropped: upstream's Makefile now
# uses $(CC) itself, so only the LuneOS service/path adaptation is still needed.
PV = "1.0+git"
SRCREV = "c3e1b155e308f6df9c9a02dbd909a44e7319ab7d"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} ${LDFLAGS}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "bluebinder.service"

do_install() {
    make install DESTDIR=${D}

    install -d ${D}${sbindir}
    install -m 0755 ${S}/bluebinder_post.sh ${D}${sbindir}/bluebinder_post.sh
    install -m 0755 ${S}/bluebinder_wait.sh ${D}${sbindir}/bluebinder_wait.sh

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/bluebinder.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} += "${sbindir}/bluebinder_post.sh ${sbindir}/bluebinder_wait.sh"
