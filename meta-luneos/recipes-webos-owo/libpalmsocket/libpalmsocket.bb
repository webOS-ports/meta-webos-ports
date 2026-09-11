# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Palm Socket Library with SSL Support"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib glib-2.0 openssl c-ares pmstatemachineengine"

PV = "2.0.0-33+git"
SRCREV = "3316409109094c4d8e61438afd32c8cef1c6cf90"

# The code quality, hardening and test harness work
# (webOS-ports/libpalmsocket#1) lives on herrie/fixes, not on the branch
# webos_ports_fork_repo defaults to, so the SRCREV above is not reachable
# from webOS-ports/master. Drop this line once herrie/fixes is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install:append() {
    # XXX Temporarily, create a link from the old include path
    install -d ${D}${includedir}/palmsocket/IncsPublic
    for i in ${D}${includedir}/palmsocket/*.h; do ln -svnf ../$(basename $i) ${D}${includedir}/palmsocket/IncsPublic; done
}
