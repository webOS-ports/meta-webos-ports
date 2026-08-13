# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Simple proxy for using android binder based bluetooth through vhci."
LICENSE = "GPL-2.0-or-later"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://bluebinder.c;beginline=1;endline=27;md5=ba7fb591c7626c434dcc691ce7797fd1"

DEPENDS = "libgbinder glib-2.0 libglibutil bluez5 systemd"
RDEPENDS:${PN} = "android-property-service"

# Rdepends on android-property-service which depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

# Rdepends on android-property-service which is MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig
inherit systemd

# 0001-Use-CC-as-compiler.patch was dropped at 1.0.20: upstream's Makefile now has
# "CC ?= $(CROSS_COMPILE)gcc" (8398480), so the environment CC is already honored.
SRC_URI = "git://github.com/mer-hybris/bluebinder.git;branch=master;protocol=https"
SRC_URI:append:tissot-halium = " file://0002-service-load-after-wifi-module-load.patch"

PV = "1.0.20"
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
