# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS library for implementing finite state machines"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib"

PV = "2.0.0-14+git"

SRCREV = "ffba7faa9b768027021f0670d41a93e26a318900"
PR = "r1"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

SRC_URI += "file://0001-Disable-using-a-version-script-as-its-causing-us-rig.patch"
