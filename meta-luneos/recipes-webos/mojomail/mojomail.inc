# Copyright (c) 2012-2013 LG Electronics, Inc.

SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "jemalloc db8 boost curl libpalmsocket libsandbox pmloglib icu json-c glib-2.0 luna-service2"

PV = "2.0.0-99+git${SRCPV}"
SRCREV = "d3a1f366d570879c3e70c5fe982eede42d85215f"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_machine_impl_dep

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
WEBOS_REPO_NAME = "mojomail"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

CXXFLAGS += "-fpermissive"

SRC_URI += "file://0001-Use-multithreaded-boost.patch;patchdir=.."
