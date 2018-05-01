# Copyright (c) 2012-2018 LG Electronics, Inc.

DESCRIPTION = "webOS component to manage all running activities."
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
LICENSE = "Apache-2.0"
SECTION = "webos/dameons"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 boost libpbnjson glib-2.0 pmloglib nyx-lib"

PV = "3.0.0-1+git${SRCPV}"
SRCREV = "6e38fc5791a7b29db3c7b43af7ac0553461cdbe6"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit webos_machine_impl_dep
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=webOS-ports/webOS-OSE"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_sysbus_datadir}"
