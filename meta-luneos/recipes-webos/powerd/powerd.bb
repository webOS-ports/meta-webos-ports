# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Power policy daemon"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c glib-2.0"

PV = "4.0.0-25+git${SRCPV}"
SRCREV = "f211f541617a6c86f080c9f717df48f27e0a19ab"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit pkgconfig
inherit webos_system_bus

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE} \
    file://0001-replace-cjson-with-json-c.patch \
"
S = "${WORKDIR}/git"
