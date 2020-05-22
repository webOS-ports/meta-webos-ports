# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "webOS of the open-source FreeBSD memory allocation library"
AUTHOR = "Rama Krishna <rama.krishna@lge.com>"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://README.md;beginline=94;md5=b159f6c7121460dba2c5965602bc3268"

PV = "0.20080828a-0webos9-1+git${SRCPV}"
SRCREV = "6989d2c72b3634a07128598aa9e222636e7843a3"

inherit webos_public_repo
inherit webos_cmake

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
