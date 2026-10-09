# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS library for implementing finite state machines"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib"

PV = "2.0.0-14+git"

SRCREV = "45d441f3074f712eafc0fe6518f3b5db3a2ccea9"
PR = "r2"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

