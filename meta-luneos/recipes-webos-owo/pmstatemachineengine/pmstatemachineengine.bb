# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS library for implementing finite state machines"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib"

PV = "2.0.0-14+git"

SRCREV = "000e8c8b24748fd0023ac50528a450f52e429c63"
WEBOS_GIT_PARAM_BRANCH = "herrie/fold-patches"
PR = "r2"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

