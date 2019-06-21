# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "An extensible object oriented component used to add service components to webOS"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"
SECTION = "webos/devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "librolegen glib-2.0 libpbnjson luna-service2"

PV = "2.0.0-2+git${SRCPV}"
SRCREV = "c58cbd2cdcdf3484442f65b7c9e072d85424d31d"

inherit webos_ports_fork_repo
inherit webos_cmake

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

ALLOW_EMPTY_${PN} = "1"
