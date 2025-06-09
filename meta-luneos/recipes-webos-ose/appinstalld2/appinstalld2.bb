# Copyright (c) 2017-2024 LG Electronics, Inc.

SUMMARY = "Application Install Service"
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib pmtrace boost icu"
RDEPENDS:${PN} = "applicationinstallerutility ecryptfs-utils librolegen"

WEBOS_VERSION = "1.0.0-48_85b593a34cefb1384ca6def806bfaf18ca92b7d9"
PR = "r7"

inherit webos_component
inherit webos_cmake
inherit webos_enhanced_submissions
inherit webos_daemon
inherit webos_system_bus
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Correctly-handle-ipk-URIs.patch \
    file://0002-AppInstaller-rescan-apps-after-install.patch \
    file://0003-AppPackage-allow-different-owner-UIDs-GIDs.patch \
    file://0004-appinstalld2-Make-org.webosports-privileged-as-well.patch \
    file://0005-Add-permission-for-rescan.patch \
"

S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "appinstalld.service"
