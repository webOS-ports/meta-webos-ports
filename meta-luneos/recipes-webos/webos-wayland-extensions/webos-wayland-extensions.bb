# Copyright (c) 2013-2023 LG Electronics, Inc.

SUMMARY = "Wayland protocol extensions for webOS"
AUTHOR = "Elvis Lee <kwangwoong.lee@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=3b9914d0b76f24145c4c707c66c944bc \
"

DEPENDS = "wayland wayland-native"

WEBOS_VERSION = "1.0.0-46_9ccf2cf894f9f5de46f110bba40aa0decb37acea"
PR = "r6"

inherit webos_cmake
inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Add-client-size-event.patch \
"
S = "${WORKDIR}/git"

FILES:${PN}-dev += "${datadir}/*"
