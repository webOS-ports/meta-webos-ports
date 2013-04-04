DESCRIPTION = "Qt bindings for the ofono dbus API"
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "qt4-webos"

SRCREV = "bcf0c04045f9db62c4e6a6070c6a26d7d68df1a9"
SRC_URI = "git://gitorious.org/meego-cellular/ofono-qt.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

PV = "1.5.0+gitr${SRCPV}"

# NOTE: We're not using qmake2.bbclass here as qt4-webos isn't compatible with it yet.
# When qt4-webos-tools-native is available or upstream changed to qt4-tools-native we can
# start to use qmake2.bbclass. Until this happens we have to add some manual
# configurations lines for qmake-palm here.

inherit webos_qmake
inherit webos_machine_dep

do_configure() {
    export STAGING_INCDIR="${STAGING_INCDIR}"
    export STAGING_LIBDIR="${STAGING_LIBDIR}"
    ${STAGING_BINDIR_NATIVE}/qmake-palm
}

do_compile_prepend() {
    export STAGING_INCDIR="${STAGING_INCDIR}"
    export STAGING_LIBDIR="${STAGING_LIBDIR}"
}
