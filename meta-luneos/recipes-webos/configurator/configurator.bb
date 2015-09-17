# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Creates the database schema for Open webOS apps"
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 json-c glib-2.0 pmloglib"

PV = "3.0.0-59+git${SRCPV}"
SRCREV = "f2c890c3b2116f61a561c6fac2a587be35a63620"

inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit webos_machine_impl_dep

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE} \
    file://0001-replace-cjson-with-json-c.patch \
"
S = "${WORKDIR}/git"

CXXFLAGS += "-fpermissive"
