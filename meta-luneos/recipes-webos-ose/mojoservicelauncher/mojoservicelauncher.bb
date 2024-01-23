# Copyright (c) 2012-2023 LG Electronics, Inc.

SUMMARY = "webOS component responsible for launching the node.js services"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/frameworks"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://oss-pkg-info.yaml;md5=bc052cc0f232b815569ebc81a4ef6490 \
"

DEPENDS = "boost libpbnjson"
RDEPENDS:${PN} = "nodejs"
# fork_server.js wants to load these:
RDEPENDS:${PN} += "nodejs-module-webos-dynaload nodejs-module-webos-pmlog nodejs-module-webos-sysbus mojoloader"

WEBOS_VERSION = "3.0.2-7_b759589be50ce52c4ae4e6af40ecbc78c7232a96"
PR = "r10"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Revert-Remove-dynaload-dependency.patch \
    file://0002-Revert-Remove-pmloglib-dependency.patch \
"
S = "${WORKDIR}/git"

FILES:${PN} += "${webos_prefix}/nodejs ${webos_servicesdir} ${webos_frameworksdir}"
