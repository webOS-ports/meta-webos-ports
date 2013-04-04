# (c) Copyright 2012-2013 Hewlett-Packard Development Company, L.P.
#               2013 Simon Busch <morphis@gravedo.de>

SUMMARY = "Qt QPA plugin for Open webOS offscreen rendering"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qt4-webos luna-sysmgr-ipc luna-sysmgr-ipc-messages libwebos-gui"

PV = "0.1.0+gitr${SRCPV}"

inherit webos_public_repo
inherit webos_qmake
inherit webos_library
inherit webos_machine_dep

SRCREV = "4c8e295bee73fe97cd96ac268bfaeab08a20f27c"
SRC_URI = "git://github.com/webOS-ports/qt-webos-plugin;branch=master;protocol=git"
S = "${WORKDIR}/git"

WEBOS_BUILD_DIR = "build-${MACHINE}"
EXTRA_OEMAKE += "-C ${WEBOS_BUILD_DIR} -f Makefile.qtwebosplugin"
export TARGET_ARCH

# Make strip into a NOP to eliminate the
#   "File 'xxx' from <component> was already stripped, this will prevent future debugging!"
# warnings.
export STRIP_TMP = ":"

do_configure() {
    # Don't trust incremental configures
    rm -rf ${WEBOS_BUILD_DIR}

    # .qmake.cache is not part of qt4-webos checkout, so let's try to create fake one, pointing to your stored stuff
    echo "QT_SOURCE_TREE = \$\$quote(${STAGING_DIR_HOST}${webos_srcdir}/qt4-webos/git)" > .qmake.cache
    echo "QT_BUILD_TREE = \$\$quote(${STAGING_DIR_HOST}${webos_srcdir}/qt4-webos/build)" >> .qmake.cache

    mkdir -p ${WEBOS_BUILD_DIR}
    (cd ${WEBOS_BUILD_DIR}; ${QMAKE} ${S}/webos.pro -o Makefile.qtwebosplugin)
}

do_compile_prepend() {
    # Workaround base_do_compile argueing cause of a missing root Makefile
    touch ${S}/Makefile
}

do_install() {
    oe_runmake INSTALL_ROOT=${D} install
}


FILES_${PN} += "${webos_qtpluginsdir}/platforms/libqwebos.so"
FILES_${PN}-dbg += "${webos_qtpluginsdir}/platforms/.debug"
