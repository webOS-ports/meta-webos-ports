# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "GLib bindings for mce."
LICENSE = "MIT"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://src/mce_display.c;beginline=1;endline=35;md5=999eef94327cfda24c3f60f25ee98fd7"

DEPENDS = "glib-2.0 glib-2.0-native libglibutil"

inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/libmce-glib.git;protocol=https;branch=master \
           https://raw.githubusercontent.com/sailfishos/mce-dev/03a4de12e5fabad8c0d846663e1a1a2cbfd88957/include/mce/dbus-names.h;name=dbus-names \
           https://raw.githubusercontent.com/sailfishos/mce-dev/03a4de12e5fabad8c0d846663e1a1a2cbfd88957/include/mce/mode-names.h;name=mode-names"
S = "${WORKDIR}/git"

SRC_URI[dbus-names.md5sum] = "2040ea70d49e679fe915dc6bc76a7aa1"
SRC_URI[dbus-names.sha256sum] = "fd890ba5921cc58c1d61b4e406f22f5d20ffb4ed6cbc8afa88881f879ad53796"
SRC_URI[mode-names.md5sum] = "b4927cca05e21114a5ba40c0d1e27f8a"
SRC_URI[mode-names.sha256sum] = "63b3b5f9966f858c9387a770021054e6a72c5ef68e181329ca25b41e29dd32a8"

PV = "1.0.7-1+git${SRCPV}"
SRCREV = "517cdafd222e3a508786174a119e6f1e5a6e4b45"

EXTRA_OEMAKE = "KEEP_SYMBOLS=1"

# We need to disable PARALLEL_MAKE because of the following

#Log data follows:
#| DEBUG: Executing shell function do_compile
#| NOTE: make -j 8 KEEP_SYMBOLS=1
#| mkdir -p build/debug
#| ln -sf libgrilio.so.1.0.35 build/debug/libgrilio.so.1
#| mkdir -p build/release
#| ln -sf libgrilio.so.1.0.35 build/release/libgrilio.so.1
#| sed -e 's/\[version\]/'1.0.35/g libgrilio.pc.in > build/libgrilio.pc
#| ln: failed to create symbolic link 'build/release/libgrilio.so.1': No such file or directory
#| Makefile:200: recipe for target 'build/release/libgrilio.so.1' failed
#| make: *** [build/release/libgrilio.so.1] Error 1
#| make: *** Waiting for unfinished jobs....
#| ERROR: oe_runmake failed

PARALLEL_MAKE = ""

do_compile:prepend() {
    install -D ${WORKDIR}/dbus-names.h ${S}/include/mce/dbus-names.h
    install -D ${WORKDIR}/mode-names.h ${S}/include/mce/mode-names.h
}

do_install() {
    oe_runmake install DESTDIR=${D}
    oe_runmake install-dev DESTDIR=${D}
}
