# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS component responsible for launching the node.js services"
AUTHOR = "Steve Lemke <steve.lemke@lge.com>"
SECTION = "webos/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "boost libpbnjson"
RDEPENDS_${PN} = "nodejs"
# fork_server.js wants to load these:
RDEPENDS_${PN} += "nodejs-module-webos-dynaload nodejs-module-webos-pmlog nodejs-module-webos-sysbus mojoloader"

PV = "3.0.1-81+git${SRCPV}"
SRCREV = "d7407a69e5acd1ac132ae25964e4db93b7685beb"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_prefix}/nodejs ${webos_servicesdir} ${webos_frameworksdir}"
FILES_${PN}-dbg += "${webos_prefix}/nodejs/.debug/*"
