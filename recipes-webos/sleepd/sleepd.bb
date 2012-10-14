# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "Sleep scheduling policy daemon"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 cjson libxml2 sqlite3 glib-2.0 powerd"

# corresponds to tag submissions/14
SRCREV = "242ce195a3c6f1530d908fb3abdce1f9f2629986"
PV = "1.1.1-14"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SRC_URI += "file://use-wakelockd-for-suspend.patch"
RDEPENDS_${PN} = "wakelockd"
