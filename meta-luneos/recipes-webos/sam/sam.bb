# Copyright (c) 2013-2021 LG Electronics, Inc.

DESCRIPTION = "System Application Manager"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson boost icu pmloglib procps libwebosi18n"
RDEPENDS:${PN} = "ecryptfs-utils"
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_webos-customization}"

VIRTUAL-RUNTIME_webos-customization ?= ""

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd
inherit webos_ports_ose_repo

PV = "2.0.0-61+git${SRCPV}"
SRCREV = "21109f653fe798ec674d2af12299f66d9c3c7ec4"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

PACKAGES =+ "${PN}-tests"
ALLOW_EMPTY:${PN}-tests = "1"
FILES:${PN}-tests = "${libexecdir}/tests/*"
FILES:${PN} += "${systemd_unitdir}/system"
