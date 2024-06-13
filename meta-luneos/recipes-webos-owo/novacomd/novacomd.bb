# Copyright (c) 2012-2024 LG Electronics, Inc.

SUMMARY = "NovaCOMd -- Daemon for NovaCOM (device and host)"
AUTHOR = "Steve Lemke <steve.lemke@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "2.0.0-124+git"
SRCREV = "471275ff1f3d584f4b46ca640a23e04b0a85389d"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_machine_impl_dep

DEPENDS  = "nyx-lib"
RDEPENDS:${PN} = "${@oe.utils.conditional('WEBOS_TARGET_MACHINE_IMPL','emulator','iproute2','',d)}"

WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"
