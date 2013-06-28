# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "Open webOS IPC library used by luna-sysmgr"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0"

# corresponds to tag submissions/1.01
SRCREV = "d62291fe7d0fda12cc57f63d82ca4eea1fce8155"
PV = "2.0.0-1.01"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_library

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
