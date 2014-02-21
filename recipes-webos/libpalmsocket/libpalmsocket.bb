# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Palm Socket Library with SSL Support"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib glib-2.0 openssl c-ares pmstatemachineengine"

WEBOS_VERSION = "2.0.0-30_dea813980f4195503d82b8295f742a4da1df8d93"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_library
inherit webos_machine_impl_dep

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    # XXX Temporarily, create a link from the old include path
    install -d ${D}${includedir}/palmsocket/IncsPublic
    for i in ${D}${includedir}/palmsocket/*.h; do ln -svnf ../$(basename $i) ${D}${includedir}/palmsocket/IncsPublic; done
}
