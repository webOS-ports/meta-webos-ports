# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "An extensible object oriented component used to add service components to webOS"
SECTION = "webos/devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "librolegen glib-2.0 libpbnjson luna-service2"

PV = "2.0.0-2+git${SRCPV}"
SRCREV = "e54e3f2d8f48dfd41d313215d8d22cd20f73380a"

inherit webos_ports_fork_repo
inherit webos_cmake

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_NO_STATIC_LIBRARIES_WHITELIST = "libserviceinstaller.a"

ALLOW_EMPTY_${PN} = "1"
