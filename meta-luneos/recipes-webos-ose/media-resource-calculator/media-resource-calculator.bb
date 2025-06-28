# Copyright (c) 2017-2025 LG Electronics, Inc.

DESCRIPTION = "Media Resource Calculator for webOS"
AUTHOR = "Sujeet Nayak <Sujeet.nayak@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "libpbnjson"

EXTRA_OECMAKE += "-DNO_TEST=1"

WEBOS_VERSION = "1.0.0-16_117e3382bf215b7728e8d16e5b9e88f6de44a211"
PR = "r8"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_pkgconfig
inherit webos_machine_dep
inherit webos_public_repo

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
          file://0001-media-resource-calculator-Add-generic-config-files.patch \
"
