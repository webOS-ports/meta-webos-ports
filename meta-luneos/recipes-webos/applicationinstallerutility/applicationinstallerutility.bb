# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY="The Application Installer Utility supports the installing and removing of applications on a webOS/LuneOS device."
AUTHOR = "Seokjun Lee <sseokjun.lee@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib openssl glib-2.0"

PV = "3.0.0-4+git${SRCPV}"
SRCREV = "151b3c18eb48e8c34478a813dac2bfcc35bd4aa4"

inherit webos_cmake
inherit webos_ports_fork_repo

WEBOS_GIT_PARAM_BRANCH = "tofe/fixes"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

