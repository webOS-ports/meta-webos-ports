# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS library for implementing finite state machines"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib"

# Kept monotonic: the openwebos patch-stack recipe shipped 2.0.0-13+git, so
# anything lower would look like a downgrade to opkg on an update.
PV = "2.0.0-14+git"

# Built from the webOS-ports fork rather than openwebos: the gcc-5 C99 inline
# fix that used to be the FsmPrv.h patch here is a commit there now (as
# "static __inline" in FsmBuildConfig.h), along with the logging, dispatch and
# warning cleanup on top of it. The branch comes from webos_ports_fork_repo
# below.
SRCREV = "9112d4dc20d5b72f7a1085add5746cc5e6b5b6be"

# The code quality and hardening work (webOS-ports/pmstatemachineengine#1)
# lives on herrie/fixes, not on the branch webos_ports_fork_repo defaults to,
# so the SRCREV above is not reachable from webOS-ports/master. Drop this line
# once herrie/fixes is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# Still needed: the fork's CMakeLists.txt continues to link against
# src/PmStateMachineEngineExports.map, which hides symbols other components
# need.
SRC_URI += "file://0001-Disable-using-a-version-script-as-its-causing-us-rig.patch"
