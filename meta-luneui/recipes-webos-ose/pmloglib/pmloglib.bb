# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "webOS logging library"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 libpbnjson"

PV = "3.3.0-13+git"
SRCREV = "2243bfb49b8feaf7d5c86b5a91345b37b007a071"
PR = "r14"

LEAD_SONAME = "libPmLogLib.so"
EXTRA_OECMAKE += "-DWEBOS_DISTRO_PRERELEASE:STRING='${WEBOS_DISTRO_PRERELEASE}'"

inherit webos_component
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_library
inherit webos_pmlog_config

PACKAGECONFIG ??= ""
PACKAGECONFIG[whitelist] = "-DENABLE_WHITELIST:BOOL=TRUE, -DENABLE_WHITELIST:BOOL=FALSE"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
