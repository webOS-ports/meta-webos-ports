# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "libsandbox is a collection of APIs for separating running programs"
AUTHOR = "Andrew Innes <andrew.innes@lge.com>"
SECTION = "webos/devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "2.0.0-1+git${SRCPV}"
SRCREV = "2f0575304c7f6dcdbc10c2a394ded29ee5217420"

inherit webos_public_repo
inherit webos_cmake
inherit webos_machine_impl_dep

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

ALLOW_EMPTY_${PN} = "1"
