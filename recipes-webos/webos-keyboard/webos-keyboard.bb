SUMMARY = "webOS on-screen keyboard based on the Ubuntu Touch keyboard"
HOMEPAGE = "https://launchpad.net/ubuntu-keyboard"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=6a6a8e020838b23406c81b19c1d46df6"

inherit qmake5

# Set path of qt5 headers as qmake5_base.bbclass sets this to just ${includedir}
# but
# actually it is ${includedir}/qt5
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

DEPENDS = "maliit-framework-qt5 hunspell presage"

RDEPENDS_${PN} += "qtsvg-plugins"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/webOS-ports/webos-keyboard;branch=master \
           file://webos-keyboard.conf \
          "
SRCREV = "ada19c5bb32a3804c9ff0653a20ec06274c0a14f"
PV = "0.99.0+git${SRCPV}"

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

do_install_append() {
    install -d ${D}${sysconfdir}/qt5/webos-ports
    install -m 0644 ${WORKDIR}/webos-keyboard.conf ${D}${sysconfdir}/qt5/webos-ports/
}

FILES_${PN} += "\
    ${libdir}/maliit \
    ${datadir} \
"

FILES_${PN}-dbg += "${libdir}/maliit/plugins/.debug"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"
