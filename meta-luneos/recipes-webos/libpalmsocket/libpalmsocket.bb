# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Palm Socket Library with SSL Support"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib glib-2.0 openssl c-ares pmstatemachineengine"

PV = "2.0.0-33+git${SRCPV}"
SRCREV = "0319a6fba4e81dd624ed22cff09a972df389f391"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_machine_impl_dep

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE} \
    file://0001-psl_build_config.h-don-t-use-extern-with-inline.patch \
    file://0002-Fix-build-for-openssl-1.1.1.patch \
"
S = "${WORKDIR}/git"

do_install:append() {
    # XXX Temporarily, create a link from the old include path
    install -d ${D}${includedir}/palmsocket/IncsPublic
    for i in ${D}${includedir}/palmsocket/*.h; do ln -svnf ../$(basename $i) ${D}${includedir}/palmsocket/IncsPublic; done
}
