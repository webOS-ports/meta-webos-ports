# Copyright (c) 2012-2018 LG Electronics, Inc.

DESCRIPTION = "webOS component to manage all running activities."
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
LICENSE = "Apache-2.0"
SECTION = "webos/dameons"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 boost libpbnjson glib-2.0 pmloglib nyx-lib"
RDEPENDS:${PN} += "bootd"

PV = "3.0.0-32+git${SRCPV}"
SRCREV = "d027d119de41e0a0a0190195c8ed98378f1476a7"

inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit webos_machine_impl_dep
inherit webos_systemd

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-activitymanager-Add-service-file-for-systemd.patch \
file://0002-Allow-for-1-minute-intervals.patch \
"
S = "${WORKDIR}/git"

FILES:${PN} += "${webos_sysbus_datadir} ${systemd_unitdir}/system"
