# Copyright (c) 2013-2014 LG Electronics, Inc.

SUMMARY = "Open webOS logging library - private interface"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "3.1.0-52+git${SRCPV}"
SRCREV = "351d8fc488337aee2b3261f02adcb7a377bb78f1"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

# OECMAKE_BUILDPATH needs to be different from that of pmloglib so there's
# no collision in the case of local development.
OECMAKE_BUILDPATH = "${S}/BUILD-${PACKAGE_ARCH}-private"
EXTRA_OECMAKE += "-DBUILD_PRIVATE=ON"

WEBOS_REPO_NAME = "pmloglib"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# Due to the nature of pmloglib-private the ${PN} package will be empty but is required
# as dependency for pmlogdaemon-dev
ALLOW_EMPTY_${PN} = "1"
