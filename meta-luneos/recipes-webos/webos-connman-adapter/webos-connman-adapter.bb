# Copyright (c) 2012-2023 LG Electronics, Inc.

DESCRIPTION = "webOS component for managing network connections using connman"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=2763f3ed850f8412903ea776e0526bea \
    file://oss-pkg-info.yaml;md5=b0cf0d697c8340cbfa56b94bdc2539fb \
"

SECTION = "webos/services"

DEPENDS = "luna-service2 libpbnjson glib-2.0 luna-prefs openssl glib-2.0-native wca-support-api wca-support nyx-lib"
RDEPENDS:${PN} = "connman connman-client"

WEBOS_VERSION = "1.1.0-42_83a6b8517c2f4ce630e4fe3d1498965cff5fbac3"
PR = "r13"

PV = "1.1.0-42+git"
SRCREV = "83a6b8517c2f4ce630e4fe3d1498965cff5fbac3"

inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

# Set EXTRA_OECMAKE in webos-connman-adapter.bbappend to override default value for wifi and wired interfaces, for eg.
# EXTRA_OECMAKE += "-DWIFI_IFACE_NAME=wlan0 -DWIRED_IFACE_NAME=eth1"

EXTRA_OECMAKE += "-DENABLE_SCAN_ON_SOFTAP=true"

PACKAGECONFIG[enable-multiple-routing-table] = "-DMULTIPLE_ROUTING_TABLE:BOOL=true,-DMULTIPLE_ROUTING_TABLE:BOOL=false,"
PACKAGECONFIG = "enable-multiple-routing-table"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "webos-connman-adapter.service"
WEBOS_SYSTEMD_SCRIPT = "webos-connman-adapter.sh"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Workaround-to-prevent-luna-call-pending.patch \
    file://0002-Add-back-com.palm.wan-for-cellular-support.patch \
    file://0003-Update-webos-connman-adapter.role.json.in-Add-permis.patch \
"

S = "${WORKDIR}/git"
