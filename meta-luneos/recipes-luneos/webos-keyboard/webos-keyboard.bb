SUMMARY = "webOS on-screen keyboard based on the Ubuntu Touch keyboard"
HOMEPAGE = "https://launchpad.net/ubuntu-keyboard"
LICENSE = "GPL-3.0 & BSD & Apache-2.0 & CC-BY-1.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=6a6a8e020838b23406c81b19c1d46df6 \
    file://COPYING.BSD;md5=9b2310382ed07cfdae9c4953c8d29078 \
    file://COPYING.Apache-2.0;beginline=37;endline=212;md5=0c4ad33a0fa7b32f42fd54ed3710d7eb \
    file://COPYING.CC-BY;md5=c14dd4d440694f070fc6520d9c9a65eb \
"

inherit qmake5
inherit webos_system_bus
inherit webos_ports_repo

DEPENDS = "maliit-framework-qt5 hunspell presage luna-service2"

RDEPENDS_${PN} += "qtsvg-plugins qtmultimedia-qmlplugins"

SRCREV = "ca1a945e3f200bffb11fef88dacb458d1f3a1723"
PV = "0.99.0+git${SRCPV}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

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

INSANE_SKIP_${PN} += "libdir staticdev"
INSANE_SKIP_${PN}-dbg += "libdir"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

FILES_${PN} += "\
    ${libdir}/maliit \
    ${datadir} \
"

FILES_${PN}-dbg += "${libdir}/maliit/plugins/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/ar/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/cs/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/da/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/de/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/es/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/en/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/fi/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/fr/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/he/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/hu/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/it/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/nl/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/pl/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/pt/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/ru/.debug \
                    ${datadir}/maliit/plugins/org/luneos/lib/sv/.debug \
                    "

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"
