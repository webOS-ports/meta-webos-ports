SUMMARY = "webOS libhybris based compositor library"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 libhybris qt4-webos"

PV = "1.0.0+gitr${SRCPV}"
WEBOS_COMPONENT_VERSION = "${PV}"
PR = "r0"

inherit webos_qmake
inherit webos_library
inherit webos_machine_dep

SRCREV = "3e175d6934c5105c169f65576db5e15bc62d8b33"
SRC_URI = "git://github.com/webOS-ports/webos-hybris-compositor;branch=master;protocol=git"
S = "${WORKDIR}/git"

WEBOS_BUILD_DIR = "build-${MACHINE}"
EXTRA_OEMAKE += "-C ${WEBOS_BUILD_DIR}"

do_configure() {
    # Don't trust incremental configures
    rm -rf ${WEBOS_BUILD_DIR}

    export STAGING_INCDIR=${includedir}
    export STAGING_LIBDIR=${libdir}

    mkdir -p ${WEBOS_BUILD_DIR}
    cd ${WEBOS_BUILD_DIR}
    ${QMAKE} ${S}/${PN}.pro
}

do_compile_append() {
    sed -i -e s:${STAGING_DIR_HOST}::g ${WEBOS_BUILD_DIR}/pkgconfig/WebosHybrisCompositor.pc
}

do_install() {
    oe_runmake INSTALL_ROOT=${D} install
}
