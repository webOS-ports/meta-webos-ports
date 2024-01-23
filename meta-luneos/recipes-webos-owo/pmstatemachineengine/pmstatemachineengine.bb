# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS library for implementing finite state machines"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib"

PV = "2.0.0-13+git${SRCPV}"
SRCREV = "a2a49bed01c9abc8545265639ae91416f15e3205"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRC_URI += "file://0001-Disable-using-a-version-script-as-its-causing-us-rig.patch \
    file://0001-FsmPrv.h-drop-FSM_CONFIG_INLINE_FUNC-and-use-normal-.patch \
"
