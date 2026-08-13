SUMMARY = "webOS on-screen keyboard based on the Ubuntu Touch keyboard"
HOMEPAGE = "https://launchpad.net/ubuntu-keyboard"
LICENSE = "LGPL-3.0-only & BSD-3-Clause & Apache-2.0 & CC-BY-3.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=6a6a8e020838b23406c81b19c1d46df6 \
    file://COPYING.BSD;md5=9b2310382ed07cfdae9c4953c8d29078 \
    file://COPYING.Apache-2.0;beginline=37;endline=212;md5=0c4ad33a0fa7b32f42fd54ed3710d7eb \
    file://COPYING.CC-BY;md5=c14dd4d440694f070fc6520d9c9a65eb \
"

inherit qt6-qmake
inherit webos_ports_repo
inherit pkgconfig

DEPENDS = "maliit-framework-webos hunspell presage luna-service2 presage-native qt5compat"

# maliit-framework-webos provides maliit-server, which hosts the keyboard plugin
# this recipe installs into ${libdir}/maliit/plugins -- without it there is no VKB
# at all. It used to arrive only as an automatic shlib dependency on
# libmaliit-plugins.so.0, but that is computed at do_package time and silently
# went missing when the maliit bump changed PKGV, so state it explicitly.
RDEPENDS:${PN} += "maliit-framework-webos qtsvg-plugins qtmultimedia-qmlplugins"
RRECOMMENDS:${PN} += "hunspell-dictionaries"

SRCREV = "19cd63f602ff978648535f5abf779682aead34fc"
PV = "0.99.2+git"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-make-it-compatible-with-newer-hunspell.patch \
    file://0002-Handle-hardware-keyboard-input.patch \
"

# a lot of cases like:
# presage.h:115:40: error: ISO C++17 does not allow dynamic exception specifications
CXXFLAGS += "-std=c++1z"

EXTRA_QMAKEVARS_PRE = "\
    PREFIX=${prefix} \
    MALIIT_INSTALL_PRF=${QMAKE_MKSPEC_PATH}/mkspecs/features \
    MALIIT_PLUGINS_DATA_DIR=${datadir} \
    LIBDIR=${libdir} \
    CONFIG+=nodoc \
    CONFIG+=notests \
    CONFIG+=enable-presage \
    CONFIG+=enable-hunspell \
"

INSANE_SKIP:${PN} += "libdir staticdev"
INSANE_SKIP:${PN}-dbg += "libdir"

FILES:${PN} += "\
    ${libdir}/maliit \
    ${datadir} \
"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"
