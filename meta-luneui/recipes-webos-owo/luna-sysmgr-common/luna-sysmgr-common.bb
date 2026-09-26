# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Library containing common parts of luna-sysmgr and webappmanager"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-prefs luna-service2 json-c nyx-lib libpbnjson sqlite3 pmloglib librolegen serviceinstaller"
DEPENDS += "qtbase"

PV = "3.0.0-4+git"

# The panel-shape keys Settings carries for a shell that has to lay out around a
# notch (Cutouts / CornerRadii in [Display]). Branched off webOS-ports/master at
# a2364531, which is the revision this recipe pinned before, so the build gets
# that plus the two new keys and nothing else. Revert to a plain master SRCREV
# once it merges.
WEBOS_GIT_PARAM_BRANCH = "herrie/panel-cutouts"
SRCREV = "fb33804c73a5ca8984ebee18076e6fe9091b34d8"

# PV carries no SRCREV, so a revision bump alone leaves PKGV untouched and opkg
# sees no upgrade on the device. Bump PR on every SRCREV move.
PR = "r4"

# Don't uncomment until all of the do_*() tasks have been moved out of the recipe
inherit webos_ports_fork_repo
inherit pkgconfig
inherit webos_cmake_qt6

inherit features_check
# luna-service2 depends on systemd
REQUIRED_DISTRO_FEATURES = "systemd"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
