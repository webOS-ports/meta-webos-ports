# Copyright (c) 2020-2023 LG Electronics, Inc.

SUMMARY = "Power manager service handles device suspend/resume/display states"
AUTHOR = "Yogish S <yogish.s@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "webos/libs"

DEPENDS = "luna-service2 glib-2.0 libpmscore virtual/pmssupportreference pmloglib libpbnjson nyx-lib"

WEBOS_VERSION = "1.0.0-11_7513b0adcb45c8a44f0ebc5b857023c8c6658451"
PR = "r2"

PV = "1.0.0-11+git${SRCPV}"
SRCREV = "7513b0adcb45c8a44f0ebc5b857023c8c6658451"

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "powermgr.service"
