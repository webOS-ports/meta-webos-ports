# Copyright (c) 2017-2024 LG Electronics, Inc.

SUMMARY = "Application Install Service"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib pmtrace boost icu"
RDEPENDS:${PN} = "applicationinstallerutility ecryptfs-utils librolegen"

WEBOS_VERSION = "1.0.0-40_398977eb4f3fd7e82561e683c959c895060b707a"
PR = "r5"

inherit webos_cmake
inherit webos_enhanced_submissions
inherit webos_system_bus
inherit webos_public_repo
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-Correctly-handle-ipk-URIs.patch \
file://0002-AppInstaller-rescan-apps-after-install.patch \
file://0003-AppPackage-allow-different-owner-UIDs-GIDs.patch \
file://0004-appinstalld2-Make-org.webosports-privileged-as-well.patch \
file://0005-Add-permission-for-rescan.patch \
file://0001-CMakeLists.txt-replace-std-c-11-with-std-c-17-for-ic.patch \
"

S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "appinstalld.service"

