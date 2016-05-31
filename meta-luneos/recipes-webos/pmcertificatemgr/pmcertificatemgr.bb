# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Provides certificate storage and wrapper API on openssl certificate manipulation functions"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "openssl glib-2.0"
RDEPENDS_${PN} = "ca-certificates"

PV = "2.0.0-29+git${SRCPV}"
SRCREV = "dc711d0788cd782e543bfa4c82eb23aee238825d"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_machine_impl_dep
inherit update-alternatives

ALTERNATIVE_${PN} = "openssl-cnf openssl-cnf2"
ALTERNATIVE_LINK_NAME[openssl-cnf] = "${libdir}/ssl/openssl.cnf"
ALTERNATIVE_PRIORITY[openssl-cnf] ?= "10"
ALTERNATIVE_LINK_NAME[openssl-cnf2] = "${sysconfdir}/ssl/openssl.cnf"
ALTERNATIVE_PRIORITY[openssl-cnf2] ?= "10"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    # We ship our own modified openssl configuration and as long as the
    # openssl-misc package is not installed within the same image we don't
    # get a conflict.
    install -d ${D}${sysconfdir}/ssl
    install -m 0644 ${S}/files/conf/ssl/openssl.cnf ${D}${sysconfdir}/ssl
}
