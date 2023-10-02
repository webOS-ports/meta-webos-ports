# Copyright (c) 2018-2023 LG Electronics, Inc.

SUMMARY = "Node-red based context intent manager (CIM)"
AUTHOR = "Muthu Santhosh Kumar <santhosh.muthu@lge.com>"
SECTION = "webos/extended-service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=fe9bd85c6f789c8ada9c8b72c7454254 \
"

DEPENDS = "nodejs-module-node-red"

WEBOS_VERSION = "1.0.0-19_15340c48143f287d667ce0c48a3ecbbad94691d1"
PR = "r6"

PV = "1.0.0-19+git"
SRCREV = "15340c48143f287d667ce0c48a3ecbbad94691d1"

inherit systemd
inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "contextintentmgr.service"
WEBOS_SYSTEMD_SCRIPT = "contextintentmgr.sh"

FILES:${PN} += "${webos_servicesdir} ${webos_sysconfdir}"
