# Copyright (c) 2022-2023 LG Electronics, Inc.

SUMMARY = "Camera Shared Memory library for webOS"
AUTHOR  = "Moorthy B S <moorthy.bs@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"
inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit pkgconfig

WEBOS_VERSION = "1.0.0-7_8ef639b99716bd1466d15d65210cc4a9c142095a"
PR = "r1"

PV = "1.0.0-7+git${SRCPV}"
SRCREV = "8ef639b99716bd1466d15d65210cc4a9c142095a"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

PACKAGECONFIG[testapp] = "-DTEST_APP=ON, -DTEST_APP=OFF, opencv, "
