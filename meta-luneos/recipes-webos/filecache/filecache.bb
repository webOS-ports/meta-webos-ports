# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Open webOS daemon to cache filesystem requests"
AUTHOR = "Alekseyev Oleksandr <alekseyev.oleksandr@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "jemalloc luna-service2 db8 glibmm boost libsandbox glib-2.0 libsigc++-2.0"

PV = "2.0.0-63+git${SRCPV}"
SRCREV = "d1c49923f6475aa4f3a04d9bb7faea3272641a32"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_machine_impl_dep
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

CXXFLAGS += "-fpermissive"
