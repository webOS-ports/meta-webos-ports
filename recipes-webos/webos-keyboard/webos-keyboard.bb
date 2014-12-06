SUMMARY = "webOS on-screen keyboard based on the Ubuntu Touch keyboard"
HOMEPAGE = "https://launchpad.net/ubuntu-keyboard"
LICENSE = "GPL-3.0 & BSD & Apache-2.0 & CC-BY-1.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=6a6a8e020838b23406c81b19c1d46df6 \
    file://COPYING.BSD;md5=9b2310382ed07cfdae9c4953c8d29078 \
    file://COPYING.Apache-2.0;beginline=37;endline=212;md5=e23fadd6ceef8c618fc1c65191d846fa \
    file://COPYING.CC-BY;md5=c14dd4d440694f070fc6520d9c9a65eb \
"

inherit qmake5
inherit webos_system_bus

# Set path of qt5 headers as qmake5_base.bbclass sets this to just ${includedir}
# but
# actually it is ${includedir}/qt5
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

DEPENDS = "maliit-framework-qt5 hunspell presage luna-service2"

RDEPENDS_${PN} += "qtsvg-plugins qtmultimedia-qmlplugins"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/webOS-ports/webos-keyboard;branch=master"
SRCREV = "439c08f30ebe90369b349fd80a80d0617ffaf580"
PV = "0.99.1+git${SRCPV}"

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

FILES_${PN} += "\
    ${libdir}/maliit \
    ${datadir} \
"

FILES_${PN}-dbg += "${libdir}/maliit/plugins/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/ar/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/cs/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/da/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/de/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/es/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/en/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/fi/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/fr/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/he/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/hu/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/it/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/nl/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/pl/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/pt/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/ru/.debug \
                    ${datadir}/maliit/plugins/com/ubuntu/lib/sv/.debug \
                    "

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"
