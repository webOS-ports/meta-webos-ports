# Copyright (c) 2017-2019 LG Electronics, Inc.

SUMMARY = "Event Monitoring for Network service"
AUTHOR = "Seokhee Lee <seokhee.lee@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 event-monitor pmloglib libpbnjson libwebosi18n"

PV = "1.0.0+git${SRCPV}"
SRCREV = "a892971d5728019c618eb49dffbfe96a02c86c5e"

inherit webos_cmake
inherit webos_event_monitor_plugin
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
