# Copyright (c) 2020-2024 LG Electronics, Inc.

SUMMARY = "Unified search service"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "luna-service2 libpbnjson glib-2.0 sqlite3"

WEBOS_VERSION = "1.0.0-14_2b434a6c598a885d262d30a978826f5cf6c59a89"
PR = "r5"

inherit webos_cmake
inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "unifiedsearch.service"

PACKAGES += "${PN}-plugins"
FILES:${PN}-plugins = "${libdir}/plugins"

EXTRA_OECMAKE += "-DUSE_BUILTIN_PLUGIN:bool=true"
