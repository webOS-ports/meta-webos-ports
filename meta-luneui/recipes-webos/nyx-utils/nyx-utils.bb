# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Command line utilities for the webOS Platform Portability Layer"
AUTHOR = "Ed Chejlava <ed.chejlava@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib glib-2.0"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_systemd
inherit pkgconfig

PV = "1.5.0-1+git${SRCPV}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRCREV = "3efc873d73c453b08b56e150268fc74bf4b013cc"

FILES:${PN} += "${libdir}/nyx/nyxcmd/"
