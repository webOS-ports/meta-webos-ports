# Copyright (c) 2020-2024 LG Electronics, Inc.

DESCRIPTION = "location framework which provides location based services implementing location handlers, plugins, and Luna location service"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/location"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=7be9908f876cc5f1edaf1124d0084067 \
"

DEPENDS = "glib-2.0 libpbnjson libxml2 pmloglib luna-service2 luna-prefs loc-utils boost"

WEBOS_VERSION = "1.0.0-105_a229e0e39ca924a091426f76108fa63f3d14c55d"
PR = "r4"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-com.webos.service.location-Add-value-for-LuneOS.patch \
    file://0002-com.webos.service.location-Fix-LS2-permission-errors.patch \
    file://0003-com.webos.service.location-Fix-paths-of-config-files.patch \
    file://0004-com.webos.service.location-include-LunaLocationServi.patch \
    file://0005-Add-back-various-API-s.patch \
    file://0006-com.webos.service.location-include-ServiceAgent.h-Fi.patch \
    file://0007-com.webos.service.location-Remove-gpsConfig-file.patch \
    file://0008-com.webos.service.location.perm.json-Update-for-Lune.patch \
"

S = "${WORKDIR}/git"

FILES:${PN} += "${libdir}/location/plugins/lib*.so"
SECURITY_STRINGFORMAT = ""
