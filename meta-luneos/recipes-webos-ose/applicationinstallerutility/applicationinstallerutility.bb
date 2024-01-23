# Copyright (c) 2013-2023 LG Electronics, Inc.

SUMMARY="The Application Installer Utility supports the installing and removing of applications on a webOS/LuneOS device."
AUTHOR = "Seokjun Lee <sseokjun.lee@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib openssl glib-2.0"

PV = "3.0.0-4+git"
SRCREV = "35ef239390b8f20272d99cff73a50af4dae59043"

inherit webos_cmake
inherit webos_public_repo
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-Provide-default-opkg-conf-path.patch \
"
S = "${WORKDIR}/git"

