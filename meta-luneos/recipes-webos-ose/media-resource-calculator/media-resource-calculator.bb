# Copyright (c) 2017-2024 LG Electronics, Inc.

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

WEBOS_VERSION = "1.0.0-13_a798d13479a193beeb682998556b2db39fe0664e"
PR = "r7"

inherit webos_cmake
inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_machine_dep

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
          file://0001-media-resource-calculator-Add-generic-config-files.patch \
"
S = "${WORKDIR}/git"
