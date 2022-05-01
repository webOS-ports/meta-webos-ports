SUMMARY = "webOS on-screen keyboard based on the Ubuntu Touch keyboard"
HOMEPAGE = "https://launchpad.net/ubuntu-keyboard"
LICENSE = "GPL-3.0-only & BSD & Apache-2.0 & CC-BY-1.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=6a6a8e020838b23406c81b19c1d46df6 \
    file://COPYING.BSD;md5=9b2310382ed07cfdae9c4953c8d29078 \
    file://COPYING.Apache-2.0;beginline=37;endline=212;md5=0c4ad33a0fa7b32f42fd54ed3710d7eb \
    file://COPYING.CC-BY;md5=c14dd4d440694f070fc6520d9c9a65eb \
"

inherit qmake5
inherit webos_ports_repo
inherit pkgconfig

DEPENDS = "maliit-framework-webos hunspell presage luna-service2 presage-native"

RDEPENDS:${PN} += "qtsvg-plugins qtmultimedia-qmlplugins"
RRECOMMENDS:${PN} += "hunspell-dictionaries"

SRCREV = "c89d6d2918d732860b38408c9bfeb5da87bb17d8"
PV = "0.99.2+git${SRCPV}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-make-it-compatible-with-newer-hunspell.patch \
"

# a lot of cases like:
# presage.h:115:40: error: ISO C++17 does not allow dynamic exception specifications
CXXFLAGS += "-std=gnu++14"

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

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"
