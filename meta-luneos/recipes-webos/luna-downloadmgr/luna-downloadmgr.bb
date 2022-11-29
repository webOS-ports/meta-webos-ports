# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY="The Download Manager service supports the downloading and uploading of files to and from a HP webOS device."
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libpbnjson luna-service2 sqlite3 curl uriparser pmloglib jemalloc luna-prefs boost glib-2.0"

RDEPENDS:${PN} = "applicationinstallerutility"

PV = "4.0.0-11+git${SRCPV}"
SRCREV = "53db994c5e00f653a3d663c50a95b566237f087b"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit webos_systemd
inherit pkgconfig


SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-luna-webappmanager-Install-to-usr-sbin-instead-of-us.patch \
file://0002-luna-downloadmanager-Fix-format-warnings-remove-unus.patch \
file://0003-Add-systemd-service-file-at-expected-location-unify-.patch \
file://0004-luna-downloadmgr-Fix-LS2-permissions.patch \
file://0005-filesystemStatusCheck-first-implementation.patch \
file://0006-DownloadManager.cpp-Make-org.webosports-privileged-a.patch \
"
S = "${WORKDIR}/git"

FILES:${PN} += " \
    ${systemd_unitdir}/system \
"
