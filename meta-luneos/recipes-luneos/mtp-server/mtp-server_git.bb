SUMMARY = "Small server implementation of MTP (based on Android)"
LICENSE = "Apache-2.0 AND GPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
    file://MODULE_LICENSE_APACHE2;md5=d41d8cd98f00b204e9800998ecf8427e \
    file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595 \
"

DEPENDS += "boost libhybris glog libunwind"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

PV = "0.0.3+git"
SRCREV = "bb4a81e61b8a21442fc6807766ec4e49e2b35000"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-Build-as-C-14-for-glog-0.7.patch \
    file://0002-Port-to-Boost.Asio-1.87.patch \
"

inherit cmake
inherit pkgconfig
inherit gettext
inherit webos_systemd
inherit webos_ports_repo

# Upstream declares cmake_minimum_required(VERSION < 3.5), which CMake 4 rejects.
# This recipe inherits plain cmake, not webos_cmake, so it needs the flag inline.
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
