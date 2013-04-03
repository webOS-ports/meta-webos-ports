# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Library for dynamically generating webOS system bus role files for webOS JavaScript services"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# corresponds to tag 18
SRCREV = "cbedc69733f65cd2f498787a621c014e219d38ab"
PV = "2.1.0-18"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions 
inherit webos_cmake
inherit webos_library

WEBOS_GIT_TAG = "${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/rolegen"
