SUMMARY = "Small server implementation of MTP (based on Android)"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
    file://MODULE_LICENSE_APACHE2;md5=d41d8cd98f00b204e9800998ecf8427e \
    file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595 \
"

DEPENDS += "boost virtual/libandroid-properties glog"

PV = "0.0.3+gitr${SRCPV}"
SRCREV = "ab1d45f472942bb8283e560f7d86605d15d56528"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit cmake
inherit webos_systemd
inherit webos_ports_repo
