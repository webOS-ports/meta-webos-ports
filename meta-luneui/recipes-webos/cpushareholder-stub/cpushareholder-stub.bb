# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Stubbed implementation of the webOS CPU shares scripts"
AUTHOR = "Maksym Shevchenko <myshevchenko@luxoft.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RPROVIDES:${PN} = "cpushareholder"

PV = "2.0.1-1+git${SRCPV}"
SRCREV = "d520ddaf1f0f571a5a660e454162cb9363e411cd"

inherit webos_public_repo
inherit allarch
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
