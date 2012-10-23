DESCRIPTION = "Connman service adapter for Open webOS"
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;md5=d103e97a58c1ee82c5051799d25fa394"

DEPENDS = "qt4-webos glib-2.0 luna-service2 connman-qt"

inherit webos_system_bus

SRCREV = "110375b2525ad1c97dca0db98174842046630663"
SRC_URI = "git://github.com/webOS-ports/connman-adapter;protocol=git;branch=master"
S = "${WORKDIR}/git"

PV = "0.1.0+gitr${SRCPV}"
PR = "r0"

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
    export MACHINE=${MACHINE}
    ${STAGING_BINDIR_NATIVE}/qmake-palm
}

do_compile_prepend() {
    export STAGING_INCDIR="${STAGING_INCDIR}"
    export STAGING_LIBDIR="${STAGING_LIBDIR}"
    export MACHINE=${MACHINE}
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/connman-adapter ${D}${bindir}

    # FIXME This should be removed once the component installs the upstart configuration
    # file itself
    install -d ${D}${sysconfdir}/event.d
    install -m 0644 ${S}/connman-adapter.upstart ${D}${sysconfdir}/event.d/connman-adapter
}
