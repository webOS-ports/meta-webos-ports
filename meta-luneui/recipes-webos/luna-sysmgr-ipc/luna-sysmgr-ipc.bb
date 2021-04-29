# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Open webOS IPC library used by luna-sysmgr"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0"

PV = "2.0.0-2+git${SRCPV}"
SRCREV = "7ad388be7f21eeff48fa384c3119c16d41859847"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRC_URI += "file://0001-ipc-fix-build-with-gcc-11.patch"
