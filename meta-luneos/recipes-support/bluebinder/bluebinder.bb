# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Simple proxy for using android binder based bluetooth through vhci."
LICENSE = "GPLv2+"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://bluebinder.c;beginline=1;endline=27;md5=430727b8efeca344ab89eeb635b4fa79"

DEPENDS = "libgbinder glib-2.0 libglibutil bluez5 systemd"
RDEPENDS:${PN} = "android-property-service"

# Rdepends on android-property-service which depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

# Rdepends on android-property-service which is MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig
inherit systemd

SRC_URI = "git://github.com/mer-hybris/bluebinder.git;branch=master;protocol=https \
           file://0001-Use-CC-as-compiler.patch \
"
SRC_URI:append:tissot = " file://0002-service-load-after-wifi-module-load.patch"

S = "${WORKDIR}/git"

PV = "1.0"
SRCREV = "419ab4a36fd61f841e7b1070b92b5e23ea813b63"

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
