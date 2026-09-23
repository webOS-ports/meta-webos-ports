# Copyright (c) 2020-2023 LG Electronics, Inc.

DESCRIPTION = "location framework which provides location based services implementing location handlers, plugins, and Luna location service"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/location"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=7be9908f876cc5f1edaf1124d0084067 \
"

DEPENDS = "glib-2.0 libpbnjson libxml2 pmloglib luna-service2 luna-prefs loc-utils boost"

SRCREV = "78f64042c2c6c4262e414d162d2205ddaa78740f"

PV = "1.0.0-109"
PR = "r3"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

FILES:${PN} += "${libdir}/location/plugins/lib*.so"
