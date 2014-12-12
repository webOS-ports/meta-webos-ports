# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Utility for drawing progress to the frame buffer"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS_${PN} = "tar"

PV = "2.0.0-22+git${SRCPV}"
SRCREV = "0dc946ef1cb557383e59e499b3ac9194dfd08b4d"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
