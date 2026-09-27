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

# Keeps the on-screen keyboard down while a hardware keyboard is the active
# input source, and adds the test and static-analysis harness. Needs the
# matching maliit-framework-webos branch: without it Maliit::Hardware is never
# selected and the new code never runs. Until this is merged, the branch rather
# than master.
WEBOS_GIT_PARAM_BRANCH = "herrie/hw-keyboard-vkb"
SRCREV = "8df85b5a73e1c9e98b5df71d4fb8c4a877b2c8bc"
PV = "0.99.2+git"
PR = "r4"

# We own webos-keyboard, so fixes belong in its actual source history, not
# as patches carried here - unlike presage or imemanager, which are genuinely
# upstream/third-party. 0001-0003 (hunspell API compat, hardware keyboard
# input, Qt6Core5Compat linking) and the db8-backed user dictionary are all
# real commits on herrie/dict-backup now; this SRCREV is the tip of that.
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
    CONFIG+=notests \
    CONFIG+=enable-presage \
    CONFIG+=enable-hunspell \
"

INSANE_SKIP:${PN} += "libdir"
INSANE_SKIP:${PN}-dbg += "libdir"

FILES:${PN} += "\
    ${libdir}/maliit \
    ${datadir} \
"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"
