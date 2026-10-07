# Copyright (c) 2019-2025 LG Electronics, Inc.

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

SRCREV = "74fe6ccdc1b89c4d1c3e1c829bcde323ba51e8f5"
PV = "1.0.0-65"
INC_PR = "r20"

inherit webos_component
inherit webos_cmake
inherit webos_ports_ose_repo
inherit webos_system_bus
inherit webos_daemon

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
