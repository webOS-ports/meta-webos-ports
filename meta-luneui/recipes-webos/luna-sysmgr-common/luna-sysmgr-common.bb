# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Library containing common parts of luna-sysmgr and webappmanager"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-prefs luna-service2 json-c nyx-lib libpbnjson sqlite3 pmloglib librolegen serviceinstaller"
DEPENDS += "luna-webkit-api"
DEPENDS += "luna-sysmgr-ipc luna-sysmgr-ipc-messages"
DEPENDS += "qtbase"

PV = "3.0.0-3+git${SRCPV}"
SRCREV = "7e27ea089058f1dcd1b889cd10de2b73cca46676"

# Don't uncomment until all of the do_*() tasks have been moved out of the recipe
inherit webos_ports_fork_repo
inherit pkgconfig
inherit webos_cmake_qt5

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
