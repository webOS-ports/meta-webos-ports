# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Library for dynamically generating webOS system bus role files for webOS JavaScript services"
SECTION = "webos/libs"
AUTHOR = "Seokhyon Seong <seokhyon.seong@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "2.1.0-20+git${SRCPV}"
SRCREV = "b293a3f8b05f83b36b1b673c4e7dd1c18bfcf768"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/rolegen"

