SUMMARY = "Small library implementing Android suspend mechanism"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "1.0.0-2+git"
SRCREV = "838ef4eafcc9bb5231584ca58ce9b636e1003712"

inherit webos_ports_repo
inherit pkgconfig
inherit cmake

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# Upstream declares cmake_minimum_required(VERSION < 3.5), which CMake 4 rejects.
# This recipe inherits plain cmake, not webos_cmake, so it needs the flag inline.
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
