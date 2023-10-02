SUMMARY = "Small server implementation of MTP (based on Android)"
LICENSE = "GPL-3.0-only & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
    file://MODULE_LICENSE_APACHE2;md5=d41d8cd98f00b204e9800998ecf8427e \
    file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595 \
"

DEPENDS += "boost libhybris glog libunwind"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PV = "0.0.3+git"
SRCREV = "bb4a81e61b8a21442fc6807766ec4e49e2b35000"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit cmake
inherit pkgconfig
inherit gettext
inherit webos_systemd
inherit webos_ports_repo
