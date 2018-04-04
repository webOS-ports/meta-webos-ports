# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Command line utilities for the webOS Platform Portability Layer"
AUTHOR = "Ed Chejlava <ed.chejlava@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib glib-2.0"

inherit webos_public_repo
inherit webos_cmake

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRCREV = "86770f142b1023f0ea88b838318f44da5107324c"

FILES_${PN} += "${libdir}/nyx/nyxcmd/"

