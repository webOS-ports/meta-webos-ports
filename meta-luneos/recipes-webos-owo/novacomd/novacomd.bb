# Copyright (c) 2012-2024 LG Electronics, Inc.

SUMMARY = "NovaCOMd -- Daemon for NovaCOM (device and host)"
AUTHOR = "Steve Lemke <steve.lemke@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "2.0.0-124+git"
SRCREV = "537338a8d9e70bb590066fafb7d1cc1c255cc10b"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig

DEPENDS  = "nyx-lib"
RDEPENDS:${PN} = "${@oe.utils.conditional('WEBOS_TARGET_MACHINE_IMPL','emulator','iproute2','',d)}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

EXTRA_OECMAKE += "-DWEBOS_TARGET_MACHINE_IMPL=hardware"

# novacomd is pre-C23 code: platform.h does "typedef int bool;" with matching
# true/false macros. GCC 15 defaults to -std=gnu23, where bool is a keyword:
#
#   include/platform.h:56:13: error: 'bool' cannot be defined via 'typedef'
#
# Build it as the C17 it was written for rather than reworking the type across
# the tree, which would change the size of bool in every struct that uses it.
CFLAGS += "-std=gnu17"
