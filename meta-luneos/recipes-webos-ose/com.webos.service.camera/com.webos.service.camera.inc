# Copyright (c) 2019-2025 LG Electronics, Inc.

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

# Built from the webOS-ports fork (webosose master + LuneOS fixes and the
# droid HAL/notifier plugins merged as commits) rather than webosose plus a
# patch stack. Pinned with a plain SRCREV: submission tags are a webosose
# convention and this branch carries none.
SRCREV = "743be909f38fb6ace643b97483f670b7edc87f03"
PV = "1.0.0-65"
INC_PR = "r18"

inherit webos_component
inherit webos_cmake
inherit webos_ports_ose_repo
inherit webos_system_bus
inherit webos_daemon

WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
