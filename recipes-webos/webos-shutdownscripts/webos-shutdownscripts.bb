# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

SUMMARY = "Open webOS shutdown scripts"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# corresponds to tag submissions/36
SRCREV = "89675ecf1c629a8438e97517adce52b0912023fd"
PV = "2.1.0-36"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_arch_indep

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
