# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "webOS logging library - private interface"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_public_repo
inherit webos_cmake

# B needs to be different from that of pmloglib so there's
# no collision in the case of local development.
B = "${S}/build-private"
EXTRA_OECMAKE += "-DBUILD_PRIVATE=ON"

WEBOS_REPO_NAME = "pmloglib"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

PV = "3.3.0-2+git${SRCPV}"
SRCREV = "42258fba451b75bd8452d0a4b038d45e9af61cc6"
