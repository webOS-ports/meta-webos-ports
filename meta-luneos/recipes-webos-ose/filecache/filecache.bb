# Copyright (c) 2012-2024 LG Electronics, Inc.

SUMMARY = "webOS daemon to cache filesystem requests"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "jemalloc luna-service2 db8 glibmm boost libsandbox glib-2.0 libsigc++-2.0"

WEBOS_VERSION = "2.0.1-13_62466fb6f04d9420e4dc65caf06fd2ae98251d29"
PR = "r6"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd
inherit pkgconfig

WEBOS_SYSTEMD_SERVICE = "filecache.service"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-filecache-Add-various-permissions-for-legacy-webOS-a.patch \
"

S = "${WORKDIR}/git"


