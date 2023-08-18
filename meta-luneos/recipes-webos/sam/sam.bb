# Copyright (c) 2013-2021 LG Electronics, Inc.

DESCRIPTION = "System Application Manager"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson boost icu pmloglib libwebosi18n"
RDEPENDS:${PN} = "ecryptfs-utils"
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_webos-customization}"

VIRTUAL-RUNTIME_webos-customization ?= ""

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd
inherit webos_public_repo

PV = "2.0.0-64+git${SRCPV}"
SRCREV = "445570c44101e97da56c169e8a9d0166f50de520"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-sam-Add-systemd-service-file.patch \
file://0002-Add-sam.sh-script-from-webos-initscripts-and-install.patch \
file://0003-com.webos.sam.role.json.in-Fix-various-outbound-perm.patch \
file://0004-Allow-getAppBasePath-also-from-trusted-apps.patch \
file://0005-RunningApp-disable-killer-timeout-for-app-relaunch.patch \
file://0006-Setup-QML-style-for-LuneOS.patch \
file://0007-Handle-noWindow-apps.patch \
file://0008-AppDescription.h-Add-org.webosports-as-privileged-as.patch \
file://0009-LuneOS-style-module-name-is-now-QtQuick.Controls.Lun.patch \
file://0010-Setup-QT_IM_MODULE-for-client-apps.patch \
file://0011-NativeContainer-configure-native-apps.patch \
file://0012-Setup-QT_WAYLAND_SHELL_INTEGRATION-for-webOS.patch \
file://0001-CMakeLists.txt-use-libproc2-from-procps-4.patch \
file://0002-sam-remove-procps-dependency.patch \
"

S = "${WORKDIR}/git"

PACKAGES =+ "${PN}-tests"
ALLOW_EMPTY:${PN}-tests = "1"
FILES:${PN}-tests = "${libexecdir}/tests/*"
FILES:${PN} += "${systemd_unitdir}/system"
