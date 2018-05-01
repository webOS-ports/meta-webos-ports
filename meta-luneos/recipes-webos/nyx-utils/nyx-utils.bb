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

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=webOS-ports/webOS-OSE"
S = "${WORKDIR}/git"

SRCREV = "0c86744f29fe18088b08fd4103666b9c4a4964aa"

FILES_${PN} += "${libdir}/nyx/nyxcmd/"
