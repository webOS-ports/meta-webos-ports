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

# Built from the webOS-ports fork (webosose master + LuneOS fixes merged as
# commits) rather than webosose plus a patch stack. Pinned with a plain
# SRCREV: submission tags are a webosose convention and this branch carries
# none. The branch itself comes from webos_ports_ose_repo below.
SRCREV = "de143e1115280295f70edd1da9136f590ff4b143"

# Set outright rather than derived from a submission tag. Kept monotonic: the
# patch-stack recipe shipped 1.1.0-50, so anything lower would look like a
# downgrade to opkg on an update.
PV = "1.1.0-51"

PR = "r22"

inherit webos_component
# The code quality and hardening work (webOS-ports/webos-connman-adapter#1)
# lives on herrie/fixes, not on the branch webos_ports_ose_repo defaults to,
# so the SRCREV above is not reachable from webOS-ports/webOS-OSE. Drop this
# line once herrie/fixes is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
inherit python3native

# Set EXTRA_OECMAKE in webos-connman-adapter.bbappend to override default value for wifi and wired interfaces, for eg.
# EXTRA_OECMAKE += "-DWIFI_IFACE_NAME=wlan0 -DWIRED_IFACE_NAME=eth1"

EXTRA_OECMAKE += "-DENABLE_SCAN_ON_SOFTAP=true"

PACKAGECONFIG[enable-multiple-routing-table] = "-DMULTIPLE_ROUTING_TABLE:BOOL=true,-DMULTIPLE_ROUTING_TABLE:BOOL=false,"
PACKAGECONFIG = "enable-multiple-routing-table"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "webos-connman-adapter.service"
WEBOS_SYSTEMD_SCRIPT = "webos-connman-adapter.sh"

# http://gecko.lge.com:8000/Errors/Details/1139929
# webos-connman-adapter/1.1.0-242.apollo.18/git/src/main.c:47:5: error: conflicting types for 'initialize_wifi_ls2_calls'; have 'int(void)'
# webos-connman-adapter/1.1.0-242.apollo.18/git/src/main.c:86:13: error: too many arguments to function 'initialize_wifi_ls2_calls'; expected 0, have 2
CFLAGS += "-std=gnu17"
