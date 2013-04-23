# (c) Copyright 2012-2013 Hewlett-Packard Development Company, L.P.
#               2013 Simon Busch <morphis@gravedo.de>

SUMMARY = "Qt QPA plugin for Open webOS rendering"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qt4-webos"

PV = "0.1.0+gitr${SRCPV}"

inherit webos_public_repo
inherit webos_qmake
inherit webos_library
inherit webos_machine_dep

SRCREV = "018fb325af82b825ab1c9c6be7f1e016a7ca3a7e"
SRC_URI = "git://github.com/webOS-ports/qt-palm-plugin;branch=master;protocol=git"
S = "${WORKDIR}/git"

PALM_BUILD_DIR = "build-${MACHINE}"
EXTRA_OEMAKE += "-C ${PALM_BUILD_DIR} -f Makefile.qtpalmplugin"
export TARGET_ARCH

# Make strip into a NOP to eliminate the
#   "File 'xxx' from <component> was already stripped, this will prevent future debugging!"
# warnings.
export STRIP_TMP = ":"

do_configure() {
    # Don't trust incremental configures
    rm -rf ${PALM_BUILD_DIR}

    # .qmake.cache is not part of qt4-webos checkout, so let's try to create fake one, pointing to your stored stuff
    echo "QT_SOURCE_TREE = \$\$quote(${STAGING_DIR_HOST}${webos_srcdir}/qt4-webos/git)" > .qmake.cache
    echo "QT_BUILD_TREE = \$\$quote(${STAGING_DIR_HOST}${webos_srcdir}/qt4-webos/build)" >> .qmake.cache
    echo "CONFIG += ${MACHINE}" >> .qmake.cache

    mkdir -p ${PALM_BUILD_DIR}
    (cd ${PALM_BUILD_DIR}; ${QMAKE} ${S}/palm.pro -o Makefile.qtpalmplugin)
}

do_compile_prepend() {
    # Workaround base_do_compile argueing cause of a missing root Makefile
    touch ${S}/Makefile
}

do_install() {
    oe_runmake INSTALL_ROOT=${D} install

    install -d ${D}${libdir}
    oe_libinstall -C ${PALM_BUILD_DIR} -so libqpalm ${D}${libdir}
}

FILES_${PN} = "${webos_qtpluginsdir}/platforms/libqpalm.so"
FILES_${PN}-dbg += "${webos_qtpluginsdir}/platforms/.debug"

# NOTE: luna-sysmgr has to be linked against libqpalm.so and as it's just a plugin it
# can't be installed in ${libdir} path as part of the default ${PN} package. Cause of this
# we put it into a separate package and make it a runtime dependency of the ${PN} package.
# Once upstream change luna-sysmgr to not link against libqpalm anymore this can be
# removed.
PACKAGES =+ "${PN}-support"
FILES_${PN}-support = "${libdir}/libqpalm.so"
RDEPENDS_${PN} += "${PN}-support"
