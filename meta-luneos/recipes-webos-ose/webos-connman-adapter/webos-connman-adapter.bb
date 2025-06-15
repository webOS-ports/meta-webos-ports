# Copyright (c) 2012-2025 LG Electronics, Inc.

DESCRIPTION = "webOS component for managing network connections using connman"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=2763f3ed850f8412903ea776e0526bea \
    file://oss-pkg-info.yaml;md5=b0cf0d697c8340cbfa56b94bdc2539fb \
"

SECTION = "webos/services"

DEPENDS = "luna-service2 libpbnjson glib-2.0 luna-prefs openssl glib-2.0-native wca-support-api wca-support nyx-lib python3-packaging-native"
RDEPENDS:${PN} = "connman connman-client"

WEBOS_VERSION = "1.1.0-50_085b6369346df4d0e6c6338b20df0a0a7ee6245f"
PR = "r20"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
inherit python3native

# Set EXTRA_OECMAKE in webos-connman-adapter.bbappend to override default value for wifi and wired interfaces, for eg.
# EXTRA_OECMAKE += "-DWIFI_IFACE_NAME=wlan0 -DWIRED_IFACE_NAME=eth1"

EXTRA_OECMAKE += "-DENABLE_SCAN_ON_SOFTAP=true"

PACKAGECONFIG[enable-multiple-routing-table] = "-DMULTIPLE_ROUTING_TABLE:BOOL=true,-DMULTIPLE_ROUTING_TABLE:BOOL=false,"
PACKAGECONFIG = "enable-multiple-routing-table"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Add-back-com.palm.wan-for-cellular-support.patch \
    file://0002-Update-webos-connman-adapter.role.json.in-Add-permis.patch \
"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "webos-connman-adapter.service"
WEBOS_SYSTEMD_SCRIPT = "webos-connman-adapter.sh"

# webos-connman-adapter/1.1.0-50/git/src/wan_service.c:587:14: error: implicit declaration of function 'cellular_technology_status_check_with_subscription'; did you mean 'p2p_technology_status_check_with_subscription'? [-Wimplicit-function-declaration]
# broken by:
# meta-luneos/recipes-webos-ose/webos-connman-adapter/webos-connman-adapter/0001-Add-back-com.palm.wan-for-cellular-support.patch
CFLAGS += "-Wno-error=implicit-function-declaration"

# http://gecko.lge.com:8000/Errors/Details/1139929
# webos-connman-adapter/1.1.0-242.apollo.18/git/src/main.c:47:5: error: conflicting types for 'initialize_wifi_ls2_calls'; have 'int(void)'
# webos-connman-adapter/1.1.0-242.apollo.18/git/src/main.c:86:13: error: too many arguments to function 'initialize_wifi_ls2_calls'; expected 0, have 2
CFLAGS += "-std=gnu17"
