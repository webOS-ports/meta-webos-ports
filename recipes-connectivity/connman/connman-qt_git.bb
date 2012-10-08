DESCRIPTION = "Qt bindings for the connman dbus API"
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qt4-webos"

SRCREV = "aeb22e6822d6a3f7beb2889901ee4ac6225ee46d"
SRC_URI = "git://github.com/morphis/libconnman-qt.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

PV = "0.2.2+gitr${SRCPV}"
PR = "r0"

# NOTE: We're not using qmake2.bbclass here as qt4-webos isn't compatible with it yet.
# When qt4-webos-tools-native is available or upstream changed to qt4-tools-native we can
# start to use qmake2.bbclass. Until this happens we have to add some manual
# configurations lines for qmake-palm here.

EXTRA_OEMAKE = " MAKEFLAGS= "

export STRIP_TMP="${STRIP}"
export F77_TMP="${F77}"
export QMAKE_MKSPEC_PATH_TMP="${QMAKE_MKSPEC_PATH}"
export CC_TMP="${CC}"
export CPPFLAGS_TMP="${CPPFLAGS}"
export RANLIB_TMP="${RANLIB}"
export CXX_TMP="${CXX}"
export OBJCOPY_TMP="${OBJCOPY}"
export CCLD_TMP="${CCLD}"
export CFLAGS_TMP="${CFLAGS}"
export TARGET_LDFLAGS_TMP="${TARGET_LDFLAGS}"
export LDFLAGS_TMP="${LDFLAGS}"
export AS_TMP="${AS}"
export AR_TMP="${AR}"
export CPP_TMP="${CPP}"
export TARGET_CPPFLAGS_TMP="${TARGET_CPPFLAGS}"
export CXXFLAGS_TMP="${CXXFLAGS}"
export OBJDUMP_TMP="${OBJDUMP}"
export LD_TMP="${LD}"

do_configure() {
    export STAGING_INCDIR="${STAGING_INCDIR}"
    export STAGING_LIBDIR="${STAGING_LIBDIR}"
    ${STAGING_BINDIR_NATIVE}/qmake-palm
}

do_compile_prepend() {
    export STAGING_INCDIR="${STAGING_INCDIR}"
    export STAGING_LIBDIR="${STAGING_LIBDIR}"
}
