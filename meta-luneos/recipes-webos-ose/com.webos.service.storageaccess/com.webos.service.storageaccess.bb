# Copyright (c) 2021-2024 LG Electronics, Inc.

SUMMARY = "Storage Access Framework for OSE"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=13b5f44cefd7b1b0040a056eeddf6174 \
"

DEPENDS= "glib-2.0 libxml2 luna-service2 pmloglib libgdrive libpbnjson curl gupnp"

WEBOS_VERSION = "1.0.0-26_b217142268de65069f5397cd9b224a18529ec884"
PR = "r8"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-CMakeLists.txt-use-pkg-config-for-libcurl-and-respec.patch \
    file://0002-CMakeLists.txt-use-gupnp-1.6.patch \
"
S = "${WORKDIR}/git"
