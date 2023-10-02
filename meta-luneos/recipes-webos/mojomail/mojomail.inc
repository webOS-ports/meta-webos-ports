# Copyright (c) 2012-2013 LG Electronics, Inc.

SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "jemalloc db8 boost curl libpalmsocket libsandbox pmloglib icu json-c glib-2.0 luna-service2"

PV = "2.0.0-99+git"
SRCREV = "979013f9c1f184553520da3b6c34c91d6b53be1a"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_machine_impl_dep

WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg-new"
WEBOS_REPO_NAME = "mojomail"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

CXXFLAGS += "-fpermissive"
