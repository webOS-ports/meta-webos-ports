DESCRIPTION = "Qt bindings for the connman dbus API"
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qt4-webos qmake-webos-native"

SRCREV = "a068f188f7dfec6ce3e20f1b4d351390b6a29c1b"
SRC_URI = " \
    git://github.com/morphis/libconnman-qt.git;protocol=git;branch=webOS-ports/master \
    file://remove-qdbusxml2cpp-dependency.patch"
S = "${WORKDIR}/git"

PV = "0.2.2+gitr${SRCPV}"

# NOTE: We're not using qmake2.bbclass here as qt4-webos isn't compatible with it yet.
# When qt4-webos-tools-native is available or upstream changed to qt4-tools-native we can
# start to use qmake2.bbclass. Until this happens we have to add some manual
# configurations lines for qmake-palm here.
inherit webos_qmake
inherit webos_machine_dep

do_configure_prepend() {
    sed -i 's/SUBDIRS += libconnman-qt test plugin/SUBDIRS += libconnman-qt plugin/g' ${S}/connman-qt.pro
}
do_configure_prepend_armv7a() {

}
do_configure() {
    export STAGING_INCDIR="${STAGING_INCDIR}"
    export STAGING_LIBDIR="${STAGING_LIBDIR}"
    ${STAGING_BINDIR_NATIVE}/qmake-palm
}

do_compile_prepend() {
    export STAGING_INCDIR="${STAGING_INCDIR}"
    export STAGING_LIBDIR="${STAGING_LIBDIR}"
}

do_install() {
    # We just want to install the library and nothing more
    cd ${S}/libconnman-qt
    oe_runmake INSTALL_ROOT=${D} install

    install -d ${D}${libdir}/pkgconfig
    mv ${D}${libdir}/connman-qt4.pc ${D}${libdir}/pkgconfig
}

FILES_${PN} += "${libdir}/libconnman-qt4.prl"
