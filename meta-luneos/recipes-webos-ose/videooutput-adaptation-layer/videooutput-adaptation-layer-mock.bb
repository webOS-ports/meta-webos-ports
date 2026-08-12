# Copyright (c) 2018-2025 LG Electronics, Inc.

SUMMARY = "Mock video output adaptation layer implementation"
DESCRIPTION = "A do-nothing VAL implementation. It satisfies the val-impl dependency of \
com.webos.service.videooutput on targets that have no real video output backend, so the \
service can be built and exercised without hardware support."
SECTION = "webos/multimedia"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

DEPENDS = "videooutput-adaptation-layer-api glib-2.0 libpbnjson pmloglib"

WEBOS_VERSION = "1.0.0-3_730d28b116759f4d28c2ac4dbc56b29c13d5452e"
PR = "r0"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
