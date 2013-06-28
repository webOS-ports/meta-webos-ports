# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "Open webOS edition of the open-source FreeBSD memory allocation library"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

# corresponds to tag submissions/11
SRCREV = "757da381d0331eea928022cc85aa4ea9c20c8ac6"
PV = "0.20080828a-0webos9-11"

inherit webos_upstream_from_repo
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_library

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}" 
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
