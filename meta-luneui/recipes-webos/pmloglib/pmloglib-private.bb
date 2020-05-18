# Copyright (c) 2013-2019 LG Electronics, Inc.

SUMMARY = "webOS logging library - private interface"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig

# B needs to be different from that of pmloglib so there's
# no collision in the case of local development.
B = "${S}/build-private"
EXTRA_OECMAKE += "-DBUILD_PRIVATE=ON"

WEBOS_REPO_NAME = "pmloglib"
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

PV = "3.3.0-2+git${SRCPV}"
SRCREV = "5907dc9dee7b561d4287ca93234e13bd9ae2dfa9"

# ${PN} package is empty
RDEPENDS_${PN}-dev = ""
