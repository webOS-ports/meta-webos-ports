# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS component responsible for launching the node.js services"
SECTION = "webos/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "boost libpbnjson"
# fork_server.js wants to load these:
RDEPENDS_${PN} = "nodejs-module-webos-dynaload nodejs-module-webos-pmlog nodejs-module-webos-sysbus mojoloader"

# corresponds to tag submissions/74
SRCREV = "4d6512dcc44a36f32131acb56c348245af59530c"
PV = "2.0.1-74"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_daemon

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_prefix}/nodejs ${webos_servicesdir} ${webos_frameworksdir}"
FILES_${PN}-dbg += "${webos_prefix}/nodejs/.debug/*"
