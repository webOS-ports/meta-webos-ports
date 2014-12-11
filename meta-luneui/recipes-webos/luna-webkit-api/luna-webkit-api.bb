# Copyright (c) 2012-2014 LG Electronics, Inc.

DESCRIPTION = "Public APIs for keyboard and field functionality in WebKit and LunaSysMgr"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "webos/devel"

PV = "2.0.0-1+git${SRCPV}"
SRCREV = "d0b393ac0c8134eabf9fcde0fa09a5c3543055c0"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit allarch

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S ="${WORKDIR}/git"

ALLOW_EMPTY_${PN} = "1"
