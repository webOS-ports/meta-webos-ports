# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "webOS component responsible for launching the node.js services"
AUTHOR = "Steve Lemke <steve.lemke@lge.com>"
SECTION = "webos/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "boost libpbnjson"
RDEPENDS_${PN} = "nodejs"
# fork_server.js wants to load these:
RDEPENDS_${PN} += "nodejs-module-webos-pmlog mojoloader"

PV = "3.0.2-2+git${SRCPV}"
SRCREV = "94fc2181880f43a6ed71c5994c1a79485aa50353"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=webOS-ports/webOS-OSE"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_prefix}/nodejs ${webos_servicesdir} ${webos_frameworksdir}"
