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
inherit webos_ports_ose_repo

PV = "1.0.0-19+git${SRCPV}"
SRCREV = "22ddf39db2dab35b82470eddc807553c603f21d9"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES:${PN} += " \ 
    ${webos_prefix} \
    ${systemd_unitdir}/system \
"
