# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "MediaTek extension for ofono-binder-plugin"
DESCRIPTION = "Connects ofono-binder-plugin to vendor.mediatek.hardware.mtkradioex \
for voice over LTE and SMS over IMS on MediaTek devices."
HOMEPAGE = "https://github.com/FuriLabs/ofono-binder-plugin-ext-mtk"
LICENSE = "GPL-2.0-only & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=111a163c3c95203b96d4659a038fe484"
SECTION = "webos/support"

DEPENDS = "ofono ofono-ext libofonobinderpluginext libgbinder libgbinder-radio libglibutil glib-2.0 libhybris"

inherit pkgconfig

# Needs libhybris for the Android properties
COMPATIBLE_MACHINE = "^halium$"

SRC_URI = "git://github.com/Herrie82/ofono-binder-plugin-ext-mtk.git;branch=forky;protocol=https"
SRCREV = "dda7590f915c250982e7b28b92cab98e5463d317"

PV = "1.2.1+git"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} "
LDFLAGS += "--sysroot=${RECIPE_SYSROOT} "

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}' PLUGINDIR=${libdir}/ofono/plugins OFONO_EXT_PKG=ofono-ext"
PARALLEL_MAKE = ""

do_install() {
    oe_runmake install DESTDIR=${D}
    install -d ${D}${sysconfdir}/ofono/binder.d
    install -m 0644 ${S}/mtk.conf ${D}${sysconfdir}/ofono/binder.d/mtk.conf
}

FILES:${PN} += "${libdir}/ofono/plugins/mtkbinderpluginext.so"

RDEPENDS:${PN} += "ofono-binder-plugin"
