# Copyright (c) 2012-2013 LG Electronics, Inc.

DESCRIPTION = "Public APIs for keyboard and field functionality in WebKit and LunaSysMgr"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "webos/devel"

# corresponds to tag submissions/1.01
SRCREV = "016455218e6cad9b5f510549d7335be88e7d3183"
PV = "2.0.0-1.01"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_arch_indep

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S ="${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "d0b393ac0c8134eabf9fcde0fa09a5c3543055c0"

ALLOW_EMPTY_${PN} = "1"

