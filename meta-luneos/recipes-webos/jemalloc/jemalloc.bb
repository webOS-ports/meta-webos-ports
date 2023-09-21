# Copyright (c) 2012-2023 LG Electronics, Inc.

SUMMARY = "webOS of the open-source FreeBSD memory allocation library"
AUTHOR = "Rama Krishna <rama.krishna@lge.com>"
SECTION = "libs"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://README.md;beginline=94;md5=b159f6c7121460dba2c5965602bc3268"

WEBOS_VERSION = "0.20080828a-0webos9-6_f527e94e1facd3a9c801bf181d5e7cba287d04bb"
PR = "r5"

PV = "0.20080828a-0webos9-6+git${SRCPV}"
SRCREV = "f527e94e1facd3a9c801bf181d5e7cba287d04bb"

inherit webos_public_repo
inherit webos_cmake

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

