# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Command line utilities for the webOS Platform Portability Layer"
AUTHOR = "Ed Chejlava <ed.chejlava@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib glib-2.0"

inherit webos_ports_repo
inherit webos_cmake
inherit webos_systemd

PV = "1.5.0-1+git${SRCPV}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO}/${PN}-1;branch=webosose"
S = "${WORKDIR}/git"

SRCREV = "b78c0d98da84de7e7a53d58b5a08738be7e6a497"

FILES_${PN} += "${libdir}/nyx/nyxcmd/"

