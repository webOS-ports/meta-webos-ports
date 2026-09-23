# Copyright (c) 2013-2025 LG Electronics, Inc.

DESCRIPTION = "System Application Manager"
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson boost icu pmloglib libwebosi18n gtest"
RDEPENDS:${PN} = "ecryptfs-utils"
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_webos-customization}"

VIRTUAL-RUNTIME_webos-customization ?= ""

SRCREV = "d43720458d0beaa63510db7f679ba726191c2605"

PV = "2.0.0-81"

PR = "r40"

inherit webos_component
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
#inherit webos_distro_variant_dep
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "sam.service"
WEBOS_SYSTEMD_SCRIPT = "sam.sh"

PACKAGECONFIG:append = " ${@bb.utils.filter('DISTRO_FEATURES', 'smack', d)}"
PACKAGECONFIG[smack] = "-Dapply_webos_smack:BOOL=True"

EXTRA_OECMAKE += "-DWEBOS_CONFIG_INSTALL_TESTS:BOOL=TRUE"

PACKAGES =+ "${PN}-tests"
ALLOW_EMPTY:${PN}-tests = "1"
FILES:${PN}-tests = "${webos_testsdir}/* ${libexecdir}/tests/*"
