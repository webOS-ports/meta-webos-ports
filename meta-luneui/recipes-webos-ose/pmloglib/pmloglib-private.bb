# Copyright (c) 2013-2025 LG Electronics, Inc.

SUMMARY = "webOS logging library - private interface"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

PV = "3.3.0-13+git"
SRCREV = "2243bfb49b8feaf7d5c86b5a91345b37b007a071"
PR = "r4"

inherit webos_component
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_pkgconfig

# B needs to be different from that of pmloglib so there's
# no collision in the case of local development.
B = "${S}/build-private"
EXTRA_OECMAKE += "-DBUILD_PRIVATE=ON"

WEBOS_REPO_NAME = "pmloglib"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
