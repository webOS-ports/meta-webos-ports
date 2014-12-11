# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Open webOS logging library"
AUTHOR = "Gayathri Srinivasan <gayathri.srinivasan@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 libpbnjson"

PV = "3.1.0-52+git${SRCPV}"
SRCREV = "351d8fc488337aee2b3261f02adcb7a377bb78f1"

WEBOS_DISTRO_PRERELEASE ??= ""
EXTRA_OECMAKE += "-DWEBOS_DISTRO_PRERELEASE:STRING='${WEBOS_DISTRO_PRERELEASE}'"
EXTRA_OECMAKE += "-DENABLE_LOGGING:BOOL='YES'"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
