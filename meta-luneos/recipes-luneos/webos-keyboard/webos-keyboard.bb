SUMMARY = "webOS on-screen keyboard based on the Ubuntu Touch keyboard"
HOMEPAGE = "https://launchpad.net/ubuntu-keyboard"
LICENSE = "LGPL-3.0-only & BSD-3-Clause & Apache-2.0 & CC-BY-3.0 & CC-BY-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=6a6a8e020838b23406c81b19c1d46df6 \
    file://COPYING.BSD;md5=9b2310382ed07cfdae9c4953c8d29078 \
    file://COPYING.Apache-2.0;beginline=37;endline=212;md5=0c4ad33a0fa7b32f42fd54ed3710d7eb \
    file://COPYING.CC-BY;md5=c14dd4d440694f070fc6520d9c9a65eb \
    file://plugins/CORPORA.md;md5=a6c8dc2742b00f66707c53ed5c95bb34 \
"
# The Pre keyboard data (Unicode-3.0) is on herrie/pre-keyboard, not yet on
# master; enable these once it is merged.
#LICENSE:append = " & Unicode-3.0"
#LIC_FILES_CHKSUM:append = " file://data/pre/README.md;beginline=16;endline=18;md5=0a2beda967c8ffe730a4393d309ddf2b"

inherit qt6-qmake
inherit webos_ports_repo
inherit pkgconfig

# sqlite3: the Pre Keyboard's swipe reads presage's word databases with it directly.
DEPENDS = "maliit-framework-webos hunspell presage luna-service2 presage-native qt5compat sqlite3"

# maliit-framework-webos provides maliit-server, which hosts the keyboard plugin
# this recipe installs into ${libdir}/maliit/plugins -- without it there is no VKB
# at all. It used to arrive only as an automatic shlib dependency on
# libmaliit-plugins.so.0, but that is computed at do_package time and silently
# went missing when the maliit bump changed PKGV, so state it explicitly.
RDEPENDS:${PN} += "maliit-framework-webos qtsvg-plugins qtmultimedia-qmlplugins"
RRECOMMENDS:${PN} += "hunspell-dictionaries"
# The Pre Keyboard's GIFs and stickers: HTTPS to KLIPY (Qt's OpenSSL TLS backend,
# in qtbase-plugins) and their previews (the GIF reader, also there, and WebP,
# in qtimageformats-plugins). Without them the tabs stay empty; nothing else.
RRECOMMENDS:${PN} += "qtbase-plugins qtimageformats-plugins"


SRCREV = "0cfafafa734eaa81aed8633b969c20f4b4e71e7f"
PE = "2"
PV = "0.99.2+git"
PR = "r34"

# We own webos-keyboard, so fixes belong in its actual source history, not
# as patches carried here - unlike presage or imemanager, which are genuinely
# upstream/third-party. 0001-0003 (hunspell API compat, hardware keyboard
# input, Qt6Core5Compat linking) and the db8-backed user dictionary are all
# real commits now (herrie/dict-backup, merged into master).
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# a lot of cases like:
# presage.h:115:40: error: ISO C++17 does not allow dynamic exception specifications
CXXFLAGS += "-std=c++1z"

EXTRA_QMAKEVARS_PRE = "\
    PREFIX=${prefix} \
    MALIIT_INSTALL_PRF=${QMAKE_MKSPEC_PATH}/mkspecs/features \
    MALIIT_PLUGINS_DATA_DIR=${datadir} \
    LIBDIR=${libdir} \
    CONFIG+=nodoc \
    ${@bb.utils.contains('PTEST_ENABLED', '1', '', 'CONFIG+=notests', d)} \
    CONFIG+=enable-presage \
    CONFIG+=enable-hunspell \
    LUNEOS_KEYBOARD_TEST_DIR=${libdir}/webos-keyboard/tests \
"

# KLIPY app key for the Pre Keyboard's GIFs and stickers. None is committed
# anywhere: set it in local.conf, or in a file it includes that no repository
# holds (luneos-secrets' secrets.conf):
#
#   KLIPY_KEY = "<key>"
#
# Make one in KLIPY's Partner Panel (docs.klipy.com, API Keys); a key in
# testing is allowed 100 calls an hour, which the keyboard keeps under. Unset,
# the GIF and sticker tabs say this build has no KLIPY key. qmake reads it from
# the environment (src/plugin/plugin.pro) and compiles it in, so it is readable
# by anyone with the image, it shows in this recipe's logs, and it is part of
# the configure and compile task hashes: an sstate cache built with it is as
# private as the key.
#
# Both tasks get it: qmake writes the top Makefile in do_configure, but the
# subdirectories' Makefiles, src/plugin's among them, only in do_compile.
KLIPY_KEY ?= ""
do_configure[vardeps] += "KLIPY_KEY"
do_compile[vardeps] += "KLIPY_KEY"

klipy_key_export() {
    case "${KLIPY_KEY}" in
        *[!A-Za-z0-9_-]*) bbfatal "KLIPY_KEY holds characters a KLIPY app key does not" ;;
    esac
    export KLIPY_KEY="${KLIPY_KEY}"
}

do_configure:prepend() {
    klipy_key_export
}

do_compile:prepend() {
    klipy_key_export
}

INSANE_SKIP:${PN} += "libdir"
INSANE_SKIP:${PN}-dbg += "libdir"

FILES:${PN} += "\
    ${libdir}/maliit \
    ${datadir} \
"

# Unit tests, under ptest only - a handful of binaries nobody wants in a shipping
# image. PACKAGES:prepend so they are claimed before FILES:${PN} sweeps ${libdir}.
PACKAGES:prepend = "${PN}-tests "
FILES:${PN}-tests = "${libdir}/webos-keyboard/tests"
RDEPENDS:${PN}-tests += "${PN}"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"
