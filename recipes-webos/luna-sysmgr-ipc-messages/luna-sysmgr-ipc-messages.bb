# Copyright (c) 2012-2014 LG Electronics, Inc.

DESCRIPTION = "Public headers for multiprocess support in LunaSysMgr"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "webos/devel"

DEPENDS = "luna-sysmgr-ipc luna-webkit-api"

WEBOS_VERSION = "2.0.0-1_26ccf5166b228b18298d0ae068fcaf858eed59dc"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit allarch

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S ="${WORKDIR}/git"

ALLOW_EMPTY_${PN} = "1"

inherit webos-ports-submissions
SRCREV = "79155bd3f19d25077d7f0ab4edc7ad6be269ec09"
