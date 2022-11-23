# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Library for dynamically generating webOS system bus role files for webOS JavaScript services"
SECTION = "webos/libs"
AUTHOR = "Seokhyon Seong <seokhyon.seong@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0"

PV = "2.1.0-20+git${SRCPV}"
SRCREV = "30e23f0c976c44c28bce0ada34639b6c289ef955"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig

WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES:${PN} += "${datadir}/rolegen"
