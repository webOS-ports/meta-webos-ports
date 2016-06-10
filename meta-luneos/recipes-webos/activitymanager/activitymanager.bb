# Copyright (c) 2012-2013 LG Electronics, Inc.

DESCRIPTION = "Open webOS component to manage all running activities."
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
LICENSE = "Apache-2.0"
SECTION = "webos/dameons"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 boost openssl glib-2.0 pmloglib nyx-lib"

PV = "3.0.0-123+git${SRCPV}"
SRCREV = "1e3e1f2a94bf57f9174b3e536b0b5c9b3a2b3d28"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit webos_machine_impl_dep
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-Fix-build-with-newer-toolchain.patch \
"
S = "${WORKDIR}/git"

CXXFLAGS += "-fpermissive"
