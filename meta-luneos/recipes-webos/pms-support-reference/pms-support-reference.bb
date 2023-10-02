# Copyright (c) 2020-2023 LG Electronics, Inc.

SUMMARY = "Reference power manager plugin"
AUTHOR = "Yogish S <yogish.s@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "webos/libs"

DEPENDS = "luna-service2 glib-2.0 pmloglib libpbnjson nyx-lib libpmscore"
PROVIDES = "virtual/pmssupportreference"

WEBOS_VERSION = "1.0.0-4_fb77973c4c82a384cee430719365b95e26de6d03"
PR = "r1"

PV = "1.0.0-4+git"
SRCREV = "fb77973c4c82a384cee430719365b95e26de6d03"

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
