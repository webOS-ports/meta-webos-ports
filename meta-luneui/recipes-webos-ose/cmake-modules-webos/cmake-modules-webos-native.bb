# Copyright (c) 2012-2025 LG Electronics, Inc.

DESCRIPTION = "CMake modules used by webOS"
LICENSE = "Apache-2.0"
AUTHOR = "Vijaya Sundaram <vijaya.sundaram@lge.com>"
SECTION = "webos/devel/tools"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

WEBOS_VERSION = "1.6.5-8_a7603cd37dcafb5d27310f3960a007a6d4ce71e9"
PR = "r0"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit native

WEBOS_CMAKE_DEPENDS = ""

# webOS OSE is dormant upstream (cmake-modules-webos has not changed since
# 2023-12), so the CMake-4 port is ours. 0001 stops webOS.cmake declaring
# compatibility with CMake < 3.5; 0002 stops webos_build_library() reading the
# LOCATION target property, which CMP0026 turned into a hard error in CMake 4
# and which CMAKE_POLICY_VERSION_MINIMUM cannot work around -- it sets the
# policy version to 3.5, at which CMP0026 is already NEW. Without 0002 every
# one of the 29 components that build a library via webos_build_library()
# fails do_configure.
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-webOS.cmake-update-cmake_minimum_required-to-3.5.0.patch \
    file://0002-webOS.cmake-do-not-read-the-LOCATION-target-property.patch \
"
S = "${WORKDIR}/git"

do_compile() {
     :
}
