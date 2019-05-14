# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "webOS daemon to cache filesystem requests"
AUTHOR = "Alekseyev Oleksandr <alekseyev.oleksandr@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "jemalloc luna-service2 db8 glibmm boost libsandbox glib-2.0 libsigc++-2.0"

PV = "2.0.1-1+git${SRCPV}"

inherit webos_ports_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_machine_impl_dep
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=webOS-ports/webOS-OSE"
S = "${WORKDIR}/git"

SRCREV = "8c61b2396bd94df11fb3b239f456de885ee66d02"
