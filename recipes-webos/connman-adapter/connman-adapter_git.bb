DESCRIPTION = "Connman service adapter for Open webOS"
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;md5=3b3ebec2970835ab0bdc6868b4e78873"

DEPENDS = "qt4-webos glib-2.0 luna-service2 connman-qt"

inherit webos_system_bus
inherit webos_qmake
inherit webos_machine_dep

SRCREV = "f7e532e77ed8d2e945467087d97e53b08f95d3ad"
SRC_URI = "git://github.com/webOS-ports/connman-adapter;protocol=git;branch=master"
S = "${WORKDIR}/git"

PV = "0.1.0+gitr${SRCPV}"

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
    install -d ${D}${webos_upstartconfdir}
    install -m 0644 ${S}/connman-adapter.upstart ${D}${webos_upstartconfdir}/connman-adapter
}
