# Copyright (c) 2015-2018 LG Electronics, Inc.

SUMMARY = "Settings Service Configs"
AUTHOR = "Denys Romanchuk <denys.romanchuk@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0.0-1+git${SRCPV}"

inherit webos_cmake
inherit webos_public_repo

WEBOS_REPO_NAME = "webos-settingsservice-conf"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

SRCREV = "5f38d96fae603216b67aa4f3362683c82ccf8579"
S = "${WORKDIR}/git"
