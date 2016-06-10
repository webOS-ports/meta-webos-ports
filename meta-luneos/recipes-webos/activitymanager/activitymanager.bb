# Copyright (c) 2012-2013 LG Electronics, Inc.

DESCRIPTION = "Open webOS component to manage all running activities."
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
LICENSE = "Apache-2.0"
SECTION = "webos/dameons"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 boost openssl glib-2.0 pmloglib nyx-lib"

PV = "3.0.0-123+git${SRCPV}"
SRCREV = "d6d1f8457036d6bf5000af4bacc176bf38b99287"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit webos_machine_impl_dep

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-Fix-build-with-newer-boost-1.58.0.patch \
    file://0001-Fix-build-with-newer-toolchain.patch \
"
S = "${WORKDIR}/git"

CXXFLAGS += "-fpermissive"
