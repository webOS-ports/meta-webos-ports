# Copyright (c) 2017-2018 LG Electronics, Inc.

SUMMARY = "Application Install Service"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib boost icu"
RDEPENDS:${PN} = "applicationinstallerutility ecryptfs-utils librolegen"

PV = "1.0.0-35+git${SRCPV}"
SRCREV = "4b8fe91a1334460ebc8c960fa78a2dfc6e8b999f"

inherit webos_cmake
inherit webos_system_bus
inherit webos_ports_ose_repo
inherit webos_systemd
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

#FILES:${PN} += " \
#    ${systemd_unitdir}/system \
#"
