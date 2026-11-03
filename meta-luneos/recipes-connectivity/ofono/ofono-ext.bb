# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "oFono extension APIs"
DESCRIPTION = "The slot manager, modem watch, cell-info and sim-info APIs \
that Sailfish OS carries inside its oFono fork, built out of tree against \
upstream oFono. ofono-binder-plugin links this rather than requiring a \
forked daemon."
HOMEPAGE = "https://github.com/webOS-ports/ofono-ext"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://src/slot-manager.c;beginline=1;endline=14;md5=236ba7557989c7a504655d359bf40a01"
SECTION = "webos/support"

DEPENDS = "ofono glib-2.0 dbus libglibutil"

# The sources are the Sailfish extension layer, taken verbatim from
# sailfishos/ofono at bea32ca9a931e77cb6d37800c06fead44265507f and
# reworked only in their #include lines. See src/ofono-ext-compat.h for
# every assumption this package makes about oFono's internals.
PV = "1.0-1+git"
# herrie/slot-manager-exit-order, pull request #1.
SRCREV = "b6c42b9c3cbd765672ff8a3b3011f32ad3be3cfe"
WEBOS_GIT_PARAM_BRANCH = "herrie/slot-manager-exit-order"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit pkgconfig

# ofonod dlopen()s plugins with RTLD_NOW, not RTLD_GLOBAL, so one plugin
# cannot resolve symbols from another. The extension APIs therefore live in
# a shared library that both the loader plugin and ofono-binder-plugin link,
# and the dynamic linker resolves it once.
EXTRA_OEMAKE = "\
    PREFIX=${prefix} LIBDIR=${libdir} \
    PLUGINDIR=${libdir}/ofono/plugins \
    STORAGEDIR=${localstatedir}/lib/ofono \
    CC='${CC}' \
"

do_install() {
    oe_runmake install DESTDIR=${D} PREFIX=${prefix} LIBDIR=${libdir} \
        PLUGINDIR=${libdir}/ofono/plugins
}

PACKAGES =+ "${PN}-plugin"

FILES:${PN}-plugin = "${libdir}/ofono/plugins/ofonoextplugin.so"
FILES:${PN} = "${libdir}/libofonoext.so.*"
FILES:${PN}-dev += "${libdir}/libofonoext.so ${libdir}/pkgconfig/ofono-ext.pc ${includedir}/ofono"

# The library binds __ofono_* symbols from ofonod at load time, so it is
# only meaningful alongside a matching oFono.
RDEPENDS:${PN} += "ofono"
RDEPENDS:${PN}-plugin += "${PN} ofono"

# Undefined symbols are expected: they are resolved from ofonod.
INSANE_SKIP:${PN} += "dev-so"
