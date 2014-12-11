# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Open webOS edition of the open-source FreeBSD memory allocation library"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

PV = "0.20080828a-0webos9-12+git${SRCPV}"
SRCREV = "41a4c8ed84c9d755c6dc7b14ad7c65fa2d5d02be"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
