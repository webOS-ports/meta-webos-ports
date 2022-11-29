# Copyright (c) 2013-2021 LG Electronics, Inc.

DESCRIPTION = "Notification Manager"
AUTHOR = "Suresh Arumugam <suresh.arumugam@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib boost libxml++"

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd
inherit webos_public_repo

PV = "1.0.0-21+git${SRCPV}"
SRCREV = "97e68e38b489ab103e68b63672b5444ee7a05d49"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-notificationmgr-Add-systemd-service-file.patch \
file://0002-Settings.cpp-Make-org.webosports-privileged-as-well.patch \
"
S = "${WORKDIR}/git"

FILES:${PN} += " \ 
    ${webos_prefix} \
    ${systemd_unitdir}/system \
"
