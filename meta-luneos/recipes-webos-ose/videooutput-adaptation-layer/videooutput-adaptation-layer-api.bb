# Copyright (c) 2018-2025 LG Electronics, Inc.

SUMMARY = "Video output adaptation layer API"
DESCRIPTION = "Headers and pkgconfig describing the interface a video output adaptation \
layer implementation has to provide for com.webos.service.videooutput."
SECTION = "webos/multimedia"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

WEBOS_VERSION = "1.0.0-4_b8ddfdc6bd7f0de06b5e35a6ee9daf1eed633642"
PR = "r0"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

# Headers and a .pc file only.
ALLOW_EMPTY:${PN} = "1"

# CMake 4: @VAR@ is no longer expanded in unquoted arguments (CMP0053),
# which broke the install() DESTINATIONs in this component.
SRC_URI += "file://0001-CMakeLists-use-CMake-variable-syntax-instead-of-VAR.patch"
